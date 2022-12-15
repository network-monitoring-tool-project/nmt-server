package nmt.backend.networkinterface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.SocketException;
import java.util.List;

@RestController
@EnableAutoConfiguration
public class NmtNetworkInterfaceController {

    @GetMapping("/api/interfaces")
    public String GetInterfaces(@RequestParam(required = true, defaultValue = "True") boolean onlyActive) throws SocketException, JsonProcessingException {

        List<NmtNetworkInterface> networkInterfaces = NmtNetworkInterfaceList.loadNetworkInterfaces();

        if (onlyActive){
            networkInterfaces = networkInterfaces.stream().filter(NmtNetworkInterface::getActiveState).toList();
        }

        //Serialization
        ObjectWriter objWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();

        return objWriter.writeValueAsString(networkInterfaces);
    }
}
