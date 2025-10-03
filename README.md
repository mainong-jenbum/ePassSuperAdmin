# 🚀 ePass Super Admin

<div align="center">
  <img src="https://img.shields.io/badge/Android-2020-brightgreen?style=for-the-badge&logo=android" alt="Android 2020"/>
  <img src="https://img.shields.io/badge/Java-8-orange?style=for-the-badge&logo=java" alt="Java 8"/>
  <img src="https://img.shields.io/badge/Retrofit-2.9.0-blue?style=for-the-badge&logo=square" alt="Retrofit"/>
  <img src="https://img.shields.io/badge/Material%20Design-3.0-purple?style=for-the-badge&logo=material-design" alt="Material Design"/>
</div>

## 📱 Overview

**ePass Super Admin** is a comprehensive Android application developed during the COVID-19 pandemic in 2020 to manage electronic travel permits and administrative functions. This super admin panel provides centralized control over ePass systems, user management, and permit approvals across different cities and divisions.

## ✨ Key Features

### 🏠 **Dashboard Management**
- **Real-time Statistics**: Live tracking of active permits, new requests, and expired permits
- **City-wise Analytics**: Comprehensive overview of permit status across different cities
- **Interactive Dashboard**: Swipe-to-refresh functionality with modern Material Design UI

### 🏛️ **Administrative Controls**
- **User Management**: Complete CRUD operations for system users
- **Division Management**: Organize and manage administrative divisions
- **City Permit Management**: City-specific permit approval workflows
- **Authority Management**: Handle different levels of administrative authority

### 📋 **Permit Management**
- **Request Processing**: Review and approve/reject travel permit requests
- **Status Tracking**: Monitor permit status (Pending, Approved, Rejected, Expired)
- **Document Management**: Handle supporting documents and signatures
- **QR Code Generation**: Generate QR codes for approved permits

### 🔧 **System Configuration**
- **Global Parameters**: Configure system-wide settings
- **Terms & Conditions**: Manage legal terms and conditions
- **File Management**: Handle document uploads and storage
- **Network Security**: Secure API communications

## 🏗️ Architecture

### **Modular Design**
```
📦 ePassSuperAdmin/
├── 📱 app/                    # Main application module
│   ├── 🎨 UI Components      # Activities, Adapters, Dialogs
│   ├── 🔧 Utilities          # Image compression, spacing utilities
│   └── 📱 App Configuration  # Application class and manifest
├── 🧩 core/                  # Shared core module
│   ├── 🌐 API Layer          # Retrofit API interfaces
│   ├── 📊 Models             # Data models and DTOs
│   ├── 🔐 Memory Management  # Shared preferences
│   └── 🛠️ Utilities         # Network, formatting utilities
└── 📋 Build Configuration    # Gradle build files
```

### **Technology Stack**
- **Language**: Java 8
- **UI Framework**: Android Material Design Components
- **Networking**: Retrofit 2.9.0 with OkHttp
- **Image Loading**: Picasso 2.71828
- **Data Binding**: Parceler for object serialization
- **Architecture**: MVVM pattern with Repository pattern
- **Build System**: Gradle 6.1.1

## 🚀 Getting Started

### **Prerequisites**
- Android Studio 4.0+
- Android SDK 29
- Java 8 or higher
- Gradle 6.1.1

### **Installation**

1. **Clone the repository**
   ```bash
   git clone https://github.com/mainong-jenbum/ePassSuperAdmin.git
   cd ePassSuperAdmin
   ```

2. **Open in Android Studio**
   ```bash
   # Open the project in Android Studio
   # The project will automatically sync dependencies
   ```

3. **Configure API Endpoints**
   ```java
   // Update API base URLs in core module
   // File: core/src/main/java/com/jenbumapps/core/utility/connect/NetworkService.java
   ```

4. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   # Install on device/emulator
   ```

## 📱 Screenshots

<div align="center">
  <img src="https://via.placeholder.com/300x600/6200EE/FFFFFF?text=Dashboard" alt="Dashboard" width="200"/>
  <img src="https://via.placeholder.com/300x600/03DAC5/FFFFFF?text=Permits" alt="Permits" width="200"/>
  <img src="https://via.placeholder.com/300x600/3700B3/FFFFFF?text=Users" alt="Users" width="200"/>
</div>

## 🔧 Configuration

### **API Configuration**
The app uses Retrofit for API communication. Configure your endpoints in:
```java
// core/src/main/java/com/jenbumapps/core/utility/connect/NetworkService.java
public class NetworkService {
    private static final String BASE_URL = "https://your-api-endpoint.com/";
    // ... API configuration
}
```

### **Build Variants**
- **Debug**: Development build with logging enabled
- **Release**: Production build with optimizations

## 📊 Data Models

### **Core Entities**
- **EPass**: Electronic travel permit with applicant details, journey information, and approval status
- **User**: System users with roles, designations, and city assignments
- **City**: Geographic divisions for permit management
- **Authority**: Administrative hierarchy for approval workflows

### **Status Management**
- **ApproveStatus**: PENDING, APPROVED, REJECTED
- **UserStatus**: ACTIVE, INACTIVE
- **FormType**: Different types of travel permits

## 🛡️ Security Features

- **Network Security Config**: HTTPS enforcement
- **Permission Management**: Granular access control
- **Data Validation**: Input sanitization and validation
- **Secure Storage**: Encrypted shared preferences

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Generate test coverage report
./gradlew jacocoTestReport
```

## 📦 Dependencies

### **Core Dependencies**
- `androidx.appcompat:appcompat:1.2.0`
- `com.google.android.material:material:1.3.0-alpha02`
- `androidx.swiperefreshlayout:swiperefreshlayout:1.1.0`

### **Networking**
- `com.squareup.retrofit2:retrofit:2.9.0`
- `com.squareup.retrofit2:converter-gson:2.8.1`
- `com.squareup.okhttp3:logging-interceptor:4.7.0`

### **Utilities**
- `com.squareup.picasso:picasso:2.71828`
- `org.parceler:parceler-api:1.1.13`
- `de.hdodenhof:circleimageview:3.1.0`

---

## 🤝 Contributing

> **⚠️ Important Notice**: This project is **no longer maintained**. It was developed as a specific solution for COVID-19 travel restrictions in 2020.

While the project is not actively maintained, the codebase serves as a reference for:
- Android multi-step form implementation
- RESTful API integration patterns
- Material Design implementation
- Firebase integration examples

---

## 📄 License

This project is developed for educational and reference purposes. Please ensure compliance with local regulations and data protection laws when using or modifying this code.

---


## 👨‍💻 Developer

**Developed by**: [[Mainong Jenbum](https://jenbum.com)]  
**Year**: 2020  
**Purpose**: COVID-19 Travel Pass Management  
**Location**: Changlang District, Arunachal Pradesh, India

---

## 🏥 Project Status

<div align="center">

![Status](https://img.shields.io/badge/Status-No%20Longer%20Maintained-red?style=for-the-badge)

**This project is no longer being maintained.**

*Developed during COVID-19 pandemic (2020) for travel pass management in Changlang District, Arunachal Pradesh. The application served its purpose during the pandemic and is now archived for reference.*

</div>

---

## 📞 Contact

For questions about this project or to discuss the implementation:

- **Email**: [mainong.jenbum@gmail.com]
- **GitHub**: [[@mainong-jenbum](https://github.com/mainong-jenbum)]
- **LinkedIn**: [[Mainong Jenbum](https://www.linkedin.com/in/mainongjenbum/)]

---

<div align="center">

**Made with ❤️ for the people of Changlang District, Arunachal Pradesh**

*During the challenging times of COVID-19 pandemic*

</div>
