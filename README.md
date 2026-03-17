# MAC Address Barcode Generator

This Android application displays a barcode of the connected host computer's MAC address.

## The Problem

Modern Android versions (10+) have significant privacy and security restrictions that prevent applications from accessing hardware identifiers, including the MAC addresses of connected devices. Attempts to read the ARP table (e.g., from `/proc/net/arp` or using the `ip neighbor` command) will fail or return empty results, even with root access in some cases, especially over USB connections where the interface might operate without using ARP (`NOARP` flag).

This makes it impossible for the Android device to automatically determine the MAC address of the computer it's connected to.

## The Solution

This project uses a workaround: the host computer (your laptop/desktop) determines its own MAC address and passes it to the Android application when it's launched via the Android Debug Bridge (ADB).

The Android app has been programmed to accept a `MAC_ADDRESS` string from the launch command's "intent extras".

## How to Use

1.  **Build and Install the App**

    Make sure your Android device is connected and has USB debugging enabled. Then, run the following command in your terminal to build and install the application:

    ```powershell
    .\gradlew installDebug
    ```

2.  **Run the Launcher Script**

    To get your computer's MAC address and launch the app with it, use the following PowerShell command:

    ```powershell
    $laptopMac = (Get-NetAdapter | Where-Object Status -eq "Up" | Select-Object -First 1).MacAddress.Replace("-", ":"); adb shell am start -S -n com.example.macqrcode/.MainActivity -e "MAC_ADDRESS" "$laptopMac"
    ```

    This command does the following:
    -   `Get-NetAdapter | ...`: Finds the first active network adapter on your Windows machine.
    -   `.MacAddress.Replace("-", ":")`: Gets its MAC address and formats it with colons.
    -   `adb shell am start ...`: Starts the application on your device, passing the MAC address as an extra string variable named `MAC_ADDRESS`.

The app will launch, and you will see the barcode for your computer's MAC address.
