# **Program License Checker**

A simple and user-friendly application to monitor and manage program licenses in a client-server environment. This tool allows clients to check the availability of licenses before using the program and provides a response if all licenses are occupied.

## 📑 **Overview**

This application features a convenient user interface that alerts users when all licenses are in use, providing them with a dialog window to confirm whether to continue or shut down the program.

---

## 🚀 **Client-Side Functionality**

1. **Setup Server LAN Addresses**  
   Clients must first configure the server’s LAN addresses.

   ![Server Setup](https://github.com/user-attachments/assets/40319787-9c5d-437b-a5d4-3d39545fc104)

2. **Test License Usage**  
   A window is provided where users can check if licenses are being used by others.

   ![Test License](https://github.com/user-attachments/assets/ac655b4d-dbe0-47ac-b728-a45e0e9d28da)

3. **Receive License Status**  
   Depending on the number of active licenses, users receive an output indicating **OK** or **WAIT** status.

   ![License Status](https://github.com/user-attachments/assets/2bb7175b-b574-4235-98d1-bf55aab00318)

---

## 🌐 **Server-Side Functionality**

1. **License Monitoring and Alerts**  
   When a license is free to use, the server gets a small pop-up indicating that other users are currently using the license.

   ![Server Pop-Up](https://github.com/user-attachments/assets/a2aff224-0f10-40b4-9c74-ae0954ec0327)

2. **Forceful Shutdown Option**  
   If a license is blocked or unavailable, the server receives an alert to confirm program shutdown. On pressing **Accept**, the program is forced to shut down.

   ![Shutdown Confirmation](https://github.com/user-attachments/assets/f1c55b30-b85a-481c-adb7-a048ff716d27)

3. **User Feedback**  
   After the server's confirmation, the user receives appropriate information about the license status.

   ![User Feedback](https://github.com/user-attachments/assets/32d70451-55f9-4a32-9c8d-a3102cbcffa2)

---
