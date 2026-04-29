# Quick Reference: Controller Refactoring Guide

## 🚀 Quick Start - 5 Step Refactoring Process

### Step 1: Replace Imports
```java
// ❌ REMOVE:
import com.antam.app.service.impl.NhanVien_Service;
import com.antam.app.connect.ConnectDB;

// ✅ ADD:
import com.antam.app.network.handler.ClientManager;
import javafx.concurrent.Task;
import java.util.logging.Logger;
import java.util.logging.Level;
```

---

### Step 2: Replace Service Fields
```java
// ❌ BEFORE:
private NhanVien_Service nhanVien_service = new NhanVien_Service();
private List<DataDTO> data = nhanVien_service.getAllNhanVien();

// ✅ AFTER:
private ClientManager clientManager;
private static final Logger LOGGER = Logger.getLogger(ControllerName.class.getName());
private ObservableList<DataDTO> data = FXCollections.observableArrayList();
```

---

### Step 3: Initialize ClientManager in Constructor
```java
// ✅ ADD AT START OF CONSTRUCTOR:
public ControllerName() {
    this.clientManager = ClientManager.getInstance();
    // ... rest of initialization ...
}
```

---

### Step 4: Replace Data Loading with Async Loading
```java
// ❌ BEFORE (synchronous - blocks UI):
public void loadData() {
    List<DataDTO> list = service.getAllData();
    tableView.setItems(FXCollections.observableArrayList(list));
}

// ✅ AFTER (asynchronous - non-blocking):
private void loadDataAsync() {
    Task<List<DataDTO>> task = new Task<List<DataDTO>>() {
        @Override
        protected List<DataDTO> call() throws Exception {
            try {
                return clientManager.getDataList();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error loading data", e);
                throw new Exception("Server connection error: " + e.getMessage());
            }
        }
    };

    task.setOnSucceeded(e -> {
        try {
            List<DataDTO> list = task.getValue();
            if (list != null) {
                data.setAll(list);
                tableView.setItems(data);
                LOGGER.info("Loaded " + list.size() + " records");
            }
        } catch (Exception ex) {
            showAlert("Error", "Failed to process data: " + ex.getMessage());
        }
    });

    task.setOnFailed(e -> {
        Throwable exception = task.getException();
        LOGGER.log(Level.SEVERE, "Data loading failed", exception);
        showAlert("Error", "Cannot load data: " + exception.getMessage());
    });

    new Thread(task).start();
}
```

---

### Step 5: Call Async Loading in Constructor
```java
// ✅ AT END OF CONSTRUCTOR:
this.setContent(root); // After UI setup
loadDataAsync(); // Load data on background thread
```

---

## 📱 ClientManager Methods Available

```java
// HoaDon
List<HoaDonDTO> getHoaDonList()
boolean insertHoaDon(HoaDonDTO dto)
boolean updateHoaDon(HoaDonDTO dto)
boolean deleteHoaDon(String maHD)

// NhanVien
List<NhanVienDTO> getNhanVienList()
boolean insertNhanVien(NhanVienDTO dto)
boolean updateNhanVien(NhanVienDTO dto)
boolean deleteNhanVien(String maNV)

// PhieuNhap
List<PhieuNhapDTO> getPhieuNhapList()
boolean insertPhieuNhap(PhieuNhapDTO dto)
boolean updatePhieuNhap(PhieuNhapDTO dto)
boolean deletePhieuNhap(String maPN)

// PhieuDat
List<PhieuDatDTO> getPhieuDatList()
boolean insertPhieuDat(PhieuDatDTO dto)
boolean updatePhieuDat(PhieuDatDTO dto)
boolean deletePhieuDat(String maPD)

// KhachHang
List<KhachHangDTO> getKhachHangList()
boolean insertKhachHang(KhachHangDTO dto)
boolean updateKhachHang(KhachHangDTO dto)
boolean deleteKhachHang(String maKH)

// Thuoc
List<ThuocDTO> getThuocList()

// Authentication
boolean login(String username, String password)
boolean logout()

// System
boolean checkServerStatus()
```

---

## 🎨 Complete Template for New Search/List Controller

```java
package com.antam.app.controller.example;

import com.antam.app.network.handler.ClientManager;
import com.antam.app.dto.DataDTO;
import javafx.concurrent.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListDataController extends ScrollPane {
    
    private static final Logger LOGGER = Logger.getLogger(ListDataController.class.getName());
    
    private ClientManager clientManager;
    private TableView<DataDTO> tableView;
    private ObservableList<DataDTO> data = FXCollections.observableArrayList();
    
    public ListDataController() {
        this.clientManager = ClientManager.getInstance();
        
        // Setup UI
        setupUI();
        
        // Setup event listeners
        setupListeners();
        
        // Load data asynchronously
        loadDataAsync();
    }
    
    private void setupUI() {
        // Create UI components here
        tableView = new TableView<>();
        // ... more UI setup ...
    }
    
    private void setupListeners() {
        // Add event listeners here
    }
    
    private void loadDataAsync() {
        Task<List<DataDTO>> task = new Task<List<DataDTO>>() {
            @Override
            protected List<DataDTO> call() throws Exception {
                try {
                    return clientManager.getDataList();
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error loading data from server", e);
                    throw new Exception("Server error: " + e.getMessage());
                }
            }
        };

        task.setOnSucceeded(e -> {
            try {
                List<DataDTO> list = task.getValue();
                if (list != null && !list.isEmpty()) {
                    data.setAll(list);
                    tableView.setItems(data);
                    LOGGER.info("Loaded " + list.size() + " records");
                } else {
                    showAlert("Info", "No data available");
                }
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error processing data", ex);
                showAlert("Error", "Failed to process data: " + ex.getMessage());
            }
        });

        task.setOnFailed(e -> {
            Throwable exception = task.getException();
            LOGGER.log(Level.SEVERE, "Task failed", exception);
            showAlert("Error", "Cannot load data: " + exception.getMessage());
        });

        new Thread(task).start();
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
```

---

## 📝 Helper Methods to Add

### Alert Box
```java
private void showAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
}
```

### Error Alert
```java
private void showErrorAlert(String title, String errorMessage) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText("An error occurred");
    alert.setContentText(errorMessage);
    alert.showAndWait();
}
```

### UI Thread Safety
```java
private void runOnUIThread(Runnable runnable) {
    javafx.application.Platform.runLater(runnable);
}
```

---

## ⚠️ Common Mistakes to Avoid

### ❌ Mistake 1: Calling UI methods from Task thread
```java
// WRONG - Will cause runtime error:
@Override
protected List<DataDTO> call() {
    List<DataDTO> list = clientManager.getDataList();
    tableView.setItems(list); // ❌ Wrong thread!
    return list;
}

// CORRECT - Use setOnSucceeded:
task.setOnSucceeded(e -> {
    tableView.setItems(task.getValue()); // ✅ Correct thread
});
```

### ❌ Mistake 2: Not handling null responses
```java
// WRONG:
List<DataDTO> list = task.getValue();
data.setAll(list); // ❌ Will crash if null!

// CORRECT:
List<DataDTO> list = task.getValue();
if (list != null) {
    data.setAll(list); // ✅ Safe
}
```

### ❌ Mistake 3: Creating new service instances
```java
// WRONG:
private NhanVien_Service service = new NhanVien_Service();

// CORRECT:
private ClientManager clientManager = ClientManager.getInstance();
```

### ❌ Mistake 4: Not logging errors
```java
// WRONG:
catch (Exception e) {
    e.printStackTrace(); // ❌ Poor visibility
}

// CORRECT:
catch (Exception e) {
    LOGGER.log(Level.SEVERE, "Error message", e); // ✅ Proper logging
}
```

---

## 🧪 Testing Checklist

After refactoring a controller, verify:

- [ ] **Compilation:** No compile errors
- [ ] **Import:** All imports correct
- [ ] **Initialization:** ClientManager initialized in constructor
- [ ] **Async Loading:** Data loads without freezing UI
- [ ] **Success:** Data displays correctly in table/list
- [ ] **Error:** Error alert shows on server failure
- [ ] **Null Check:** Handles null responses gracefully
- [ ] **Logging:** Logs appear in console/logs
- [ ] **Threading:** No cross-thread access violations
- [ ] **Memory:** No memory leaks on reload

---

## 🎯 Refactoring Order

**Priority 1** (Today):
1. TimPhieuDatController
2. ThemDangDieuCheController

**Priority 2** (Tomorrow):
1. XemChiTietPhieuDatFormController
2. XemChiTietPhieuNhapFormController

**Priority 3** (Next Day):
1. ThemPhieuNhapFormController
2. ThemPhieuDatController (Complex)

---

## 💡 Pro Tips

1. **Copy-Paste Template:** Use the template above for consistency
2. **Git Commits:** Commit each controller separately
3. **Test First:** Test before committing
4. **Documentation:** Update comments when refactoring
5. **Consistency:** Follow same pattern for all controllers
6. **Code Review:** Have someone review before merge

---

## 📞 Quick Help

**Q: Controller won't load data?**
- A: Check server is running: `mvn exec:java -Dexec.mainClass="com.antam.app.network.ServerMain"`

**Q: Getting "ClientManager not found"?**
- A: Verify `ClientManager.java` exists in `src/main/java/com/antam/app/network/handler/`

**Q: Data appears delayed?**
- A: This is normal - data loads on background thread. Add a "Loading..." indicator if needed.

**Q: How to test locally?**
- A: 
  1. Start server: `mvn exec:java -Dexec.mainClass="com.antam.app.network.ServerMain"`
  2. Run app: `mvn javafx:run`
  3. Navigate to controller
  4. Verify data loads

---

## 📚 Related Documents

- `CONTROLLER_REFACTOR_SUMMARY.md` - Detailed example
- `CONTROLLER_REFACTORING_PLAN.md` - Full plan & checklist
- `CLIENT_SERVER_ARCHITECTURE.md` - Architecture overview
- `IMPLEMENTATION_GUIDE.md` - Getting started

---

**Last Updated:** 28/04/2026  
**Version:** 1.0  
**Use For:** Refactoring remaining 18+ controllers

