package nmt.backend.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    final static int MAX_BYTE = 255;
    final static int MAX_BYTES = 4;
    final static int MAX_BIT = 8;
    final static int[] ZERO_MASK = {0, 0, 0, 0};

    public static List<String> getAvailableAddresses(final String ip, final int cidr) {
        final List<String> ipAddresses = new ArrayList<>();

        final String[] ipArr = ip.split("\\.");
        final int[] mask = getMaskAsArray(cidr);

        StringBuilder address = new StringBuilder();

        for (int ipByte = 0; ipByte < ipArr.length; ipByte++) {
            if (mask[ipByte] < MAX_BYTE) {
                // add all blocks
                addNextBlock(address, mask[ipByte], ipByte, MAX_BYTE, ipAddresses);
                break;
            } else {
                address
                        .append(ipArr[ipByte])
                        .append(".");
            }
        }
        return ipAddresses;
    }

    public static List<String> getAvailableAddresses(final String lowerBound, final String upperBound) throws NumberFormatException {
        final List<String> ipAddresses = new ArrayList<>();

        final int[] lowerIpArr = Arrays.stream(lowerBound.split("\\.")).mapToInt(Integer::parseInt).toArray();
        final int[] countingIpArr = Arrays.copyOf(lowerIpArr, lowerIpArr.length);
        final int[] upperIpArr = Arrays.stream(upperBound.split("\\.")).mapToInt(Integer::parseInt).toArray();

        // each ip array has to contain exactly 4 elements (4 * 8 Bit = 32 Bit)
        if (lowerIpArr.length != MAX_BYTES || (upperIpArr.length != MAX_BYTES)) {
            return ipAddresses;
        }

        ipAddresses.add(getAddressFromArr(lowerIpArr));
        while (!Arrays.equals(countingIpArr, upperIpArr)) {
            countingIpArr[MAX_BYTES - 1]++;

            for (int c = MAX_BYTES - 1; c >= 0; c--) {
                if (countingIpArr[c] > 255) {
                    countingIpArr[c] = 0;
                    if (c - 1 >= 0)
                        countingIpArr[c - 1]++;
                }
            }
            ipAddresses.add(getAddressFromArr(countingIpArr));
        }
        return ipAddresses;
    }

    public static String getAddressFromArr(int[] arr) {
        StringBuilder b = new StringBuilder();
        Arrays.stream(arr).forEach(l -> b.append(l).append("."));

        b.deleteCharAt(b.length() - 1);
        return b.toString();
    }

    /**
     * Internal function to recursively add up all ip addresses once a subnet mask limits the coverage.
     *
     * @param address    uncompleted ip address string
     * @param mask       decimal value of the according byte of the subnet mask
     * @param byteNum    position of the according byte of the subnet mask
     * @param upperBound max. decimal value a byte can have
     * @param addresses  list of ready built ip addresses
     */
    private static String addNextBlock(StringBuilder address, int mask, int byteNum, int upperBound, List<String> addresses) {
        final String old = address.toString();
        for (int i = mask; i <= upperBound; i++) {
            if (byteNum + 1 < 4) {
                address.append(addNextBlock(new StringBuilder(old + i), 0, byteNum + 1, upperBound, addresses));
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
        int[] maskArray = Arrays.copyOf(ZERO_MASK, ZERO_MASK.length);
        int i = 0;
        while (cidr >= MAX_BIT) {
            maskArray[i] = MAX_BYTE;
            i += 1;
            cidr -= MAX_BIT;
        }

        maskArray[i] = Integer.parseInt(buildLeftRightStr("1", "0", cidr, MAX_BIT), 2);
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
