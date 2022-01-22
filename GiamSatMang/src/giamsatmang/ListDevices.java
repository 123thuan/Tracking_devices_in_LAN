package giamsatmang;

import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

public class ListDevices {

    public static void excute() throws InterruptedException {
        while (true) {
            Context context = new Context();

            int result = LibUsb.init(context);
            if (result < 0) {
                throw new LibUsbException("Không khởi tạo được libusb", result);
            }

            // Read the USB device list
            DeviceList list = new DeviceList();
            result = LibUsb.getDeviceList(context, list);
            if (result < 0) {
                throw new LibUsbException("Không thể lấy danh sách thiết bị", result);
            }
            try {
                for (Device device : list) {
                    int address = LibUsb.getDeviceAddress(device);
                    int busNumber = LibUsb.getBusNumber(device);
                    DeviceDescriptor descriptor = new DeviceDescriptor();
                    result = LibUsb.getDeviceDescriptor(device, descriptor);
                    if (result < 0) {
                        throw new LibUsbException("Không thể đọc bộ mô tả thiết bị", result);
                    }
                System.out.format("Bus %03d, Device %03d: Vendor %04x, Product %04x, Manufacturer %04x, bcdUSB %04x%n",
                                busNumber, address, descriptor.idVendor(),
                                descriptor.idProduct(),descriptor.iManufacturer(),descriptor.bcdUSB());

                    short vId = descriptor.idVendor();
                    short pId = descriptor.idProduct();
                    

                    String hexVId = Integer.toHexString(vId & 0xffff);
                    String hexPId = Integer.toHexString(pId & 0xffff);
                    System.out.println(add(hexVId));
                    System.out.println(add(hexPId));

                    CrawData http = new CrawData(add(hexVId), add(hexPId));
                    hexVId = "";
                    hexPId = "";
                    Thread.sleep(2000);;
                }
            } finally {
                // Ensure the allocated device list is freed
                LibUsb.freeDeviceList(list, true);
            }

            // Deinitialize the libusb context
            LibUsb.exit(context);
            Thread.sleep(180000);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Create the libusb context
        excute();

    }

    private static String add(String str) {
        switch (str.length()) {
            case 4:
                str = "0x" + str;
                break;
            case 3:
                str = "0x0" + str;
                break;
            case 2:
                str = "0x00" + str;
                break;
            default:
                str = "0x000" + str;
                break;
        }
        return str;

    }
}
