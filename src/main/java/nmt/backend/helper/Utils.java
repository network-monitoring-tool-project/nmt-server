package nmt.backend.helper;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<String> getAvailableAddresses(final String ip, final int cidr) {
        List<String> ipAddresses = new ArrayList<>();

        final String[] ipArr = ip.split("\\.");
        final int[] mask = getMaskAsArray(cidr);

        StringBuilder address = new StringBuilder();

        for (int ipByte = 0; ipByte < ipArr.length; ipByte++) {
            if (mask[ipByte] < 255) {
                // add all blocks
                addNextBlock(address, mask[ipByte], ipByte, ipAddresses);
                break;
            } else {
                address
                        .append(ipArr[ipByte])
                        .append(".");
            }
        }
        return ipAddresses;
    }

    private static String addNextBlock(StringBuilder address, int mask, int byteNum, List<String> addresses) {
        final String old = address.toString();
        for (int i = mask; i <= 255; i++) {
            if (byteNum + 1 < 4) {
                address.append(addNextBlock(new StringBuilder(old + i), 0, byteNum + 1, addresses));
            } else {
                addresses.add(String.format("%s.%s", address, i));
            }
        }

        return address.toString();
    }

    /**
     * Builds an int[3] array from given CIDR representing the subnet mask.
     *
     * @param cidr CIDR number of the subnet mask
     */
    public static int[] getMaskAsArray(int cidr) {
        int[] maskArray = {0, 0, 0, 0};
        int i = 0;
        while (cidr >= 8) {
            maskArray[i] = 255;
            i += 1;
            cidr -= 8;
        }

        maskArray[i] = Integer.parseInt(buildLeftRightStr("1", "0", cidr, 8), 2);
        return maskArray;
    }

    /**
     * Builds a new string given the in param <b>left</b> and <b>right</b> specified chars.
     *
     * @param left    left part of the string
     * @param right   right part of the string
     * @param leftLen length of the left part
     * @param len     full length of the new string
     */
    public static String buildLeftRightStr(final String left, final String right, final int leftLen, final int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= len; i++) {
            sb.append(i <= leftLen ? left : right);
        }
        return sb.toString();
    }

}
