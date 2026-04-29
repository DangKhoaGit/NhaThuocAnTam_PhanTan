# Controller Refactoring Priority Plan

## Status: In Progress

Total Controllers to Refactor: **20+ occurrences** across multiple files

---

## Priority Queue (High → Low)

### 🔴 CRITICAL - Search/List Controllers (Should be async)
These display data and need async loading to prevent UI freeze:

1. **`TimPhieuNhapController.java`** ⭐ HIGH
   - Uses: `PhieuNhap_Service`, `NhanVien_Service`
   - Status: ❌ To-do
   - Lines: ~48-49
   - Reason: User searches for purchase orders, needs async loading

2. **`TimPhieuDatController.java`** ⭐ HIGH
   - Uses: `PhieuDat_Service`, `NhanVien_Service`
   - Status: ❌ To-do
   - Lines: ~63-65
   - Reason: User searches for order forms, needs async loading

3. **`ThemDangDieuCheController.java`** ⭐ MEDIUM-HIGH
   - Uses: `DangDieuChe_Service`
   - Status: ❌ To-do
   - Lines: ~34
   - Reason: Displays list of usage indications

---

### 🟡 MEDIUM - Form Controllers (CRUD Operations)
These handle create/update/delete operations:

4. **`ThemPhieuNhapFormController.java`** ⭐ MEDIUM
   - Uses: `Thuoc_Service`
   - Status: ❌ To-do
   - Lines: ~41
   - Reason: Form for creating purchase invoices

5. **`ThemPhieuDatFormController.java`** ⭐ MEDIUM
   - Uses: Multiple services (Thuoc, DonViTinh, KhachHang, KhuyenMai, LoThuoc, HoaDon, PhieuDat, ChiTietPhieuDat)
   - Status: ❌ To-do
   - Lines: ~63-76, 663, 677, 740
   - Reason: Complex form with multiple dependencies

6. **`XemChiTietPhieuDatFormController.java`** ⭐ MEDIUM
   - Uses: `ChiTietPhieuDat_Service`
   - Status: ❌ To-do
   - Lines: ~48

7. **`XemChiTietPhieuNhapFormController.java`** ⭐ MEDIUM
   - Uses: `PhieuNhap_Service`, `ChiTietPhieuNhap_Service`
   - Status: ❌ To-do
   - Lines: ~43-44

8. **`ThemPhieuDatController.java`** ⭐ MEDIUM
   - Uses: `NhanVien_Service`, `PhieuDat_Service`
   - Status: ❌ To-do
   - Lines: ~63-64

---

## Refactoring Template

Each controller should follow this pattern:

```java
// 1. Add imports
import com.antam.app.network.handler.ClientManager;
import javafx.concurrent.Task;
import java.util.logging.Logger;
import java.util.logging.Level;

// 2. Replace service instantiation
// ❌ BEFORE:
private DataService dataService = new DataService();

// ✅ AFTER:
private ClientManager clientManager;
private static final Logger LOGGER = Logger.getLogger(ControllerName.class.getName());

// 3. Initialize in constructor
public ControllerName() {
    this.clientManager = ClientManager.getInstance();
    // ... setup UI
    loadDataAsync(); // Load data asynchronously
}

// 4. Async loading method
private void loadDataAsync() {
    Task<List<DataDTO>> task = new Task<List<DataDTO>>() {
        @Override
        protected List<DataDTO> call() throws Exception {
            return clientManager.getDataList(); // Call ClientManager method
        }
    };

    task.setOnSucceeded(e -> {
        // Update UI with task.getValue()
    });

    task.setOnFailed(e -> {
        showAlert("Error", task.getException().getMessage());
    });

    new Thread(task).start();
}

// 5. For CRUD operations (Create/Update/Delete)
private void saveData(DataDTO dto) {
    Task<Boolean> task = new Task<Boolean>() {
        @Override
        protected Boolean call() throws Exception {
            return clientManager.insertData(dto); // or updateData, deleteData
        }
    };

    task.setOnSucceeded(e -> {
        if (task.getValue()) {
            showAlert("Thành công", "Dữ liệu đã được lưu");
            loadDataAsync(); // Refresh list
        }
    });

    task.setOnFailed(e -> {
        showAlert("Lỗi", "Lỗi lưu dữ liệu: " + task.getException().getMessage());
    });

    new Thread(task).start();
}
```

---

## Implementation Checklist

### Phase 1: Search Controllers (Week 1)
- [ ] `TimPhieuNhapController.java`
- [ ] `TimPhieuDatController.java`
- [ ] `ThemDangDieuCheController.java`

### Phase 2: Simple Form Controllers (Week 2)
- [ ] `ThemPhieuNhapFormController.java`
- [ ] `XemChiTietPhieuDatFormController.java`
- [ ] `XemChiTietPhieuNhapFormController.java`

### Phase 3: Complex Form Controllers (Week 3)
- [ ] `ThemPhieuDatFormController.java` (Complex - multiple services)
- [ ] `ThemPhieuDatController.java`

---

## Common Issues & Solutions

### Issue 1: Multiple Service Dependencies
Some forms use 5-8 services. Solution:
- Use separate async tasks for each operation
- Or batch load all data at once then combine in controller

### Issue 2: CRUD Operations in Forms
For save/update/delete:
- Wrap in Task
- Show loading indicator
- Refresh list on success
- Show error alert on failure

### Issue 3: Dependent Data Loading
If Field B depends on Field A:
- Load Field A first
- On success, load Field B
- Or use nested tasks

### Issue 4: Form Validation Before Save
- Validate data before creating Task
- Show inline error messages
- Don't make network call if invalid

---

## Completed Refactors ✅

1. **`TimNhanVienController.java`** ✅ DONE
   - Migrated to ClientManager
   - Implemented async loading with Task
   - Added proper error handling
   - Added logging throughout
   - Status: ✅ Production Ready

---

## Code Review Checklist for Each Controller

Before marking complete:
- [ ] No direct service instantiation
- [ ] Uses ClientManager.getInstance()
- [ ] Data loading is async with Task
- [ ] Has success handler (setOnSucceeded)
- [ ] Has failure handler (setOnFailed)
- [ ] UI updates on JavaFX thread only
- [ ] Proper exception handling
- [ ] Logging added for debugging
- [ ] User-friendly error messages
- [ ] Code compiles without warnings
- [ ] UI doesn't freeze during operations

---

## Testing Strategy

### Unit Testing
```java
@Test
public void testAsyncDataLoading() {
    // Mock ClientManager
    // Verify task completes successfully
    // Verify UI updates with correct data
}

@Test
public void testErrorHandling() {
    // Mock ClientManager to throw exception
    // Verify error alert is shown
}
```

### Integration Testing
1. Start server
2. Start application
3. Load controller
4. Verify data populates
5. Try filtering/searching
6. Test error scenarios (server down, network error)

---

## Resources

- **Reference Controller:** `TimNhanVienController.java` (Already refactored ✅)
- **Architecture Doc:** `CLIENT_SERVER_ARCHITECTURE.md`
- **Implementation Guide:** `IMPLEMENTATION_GUIDE.md`
- **Refactoring Summary:** `CONTROLLER_REFACTOR_SUMMARY.md`

---

## Next Steps

1. Review this plan with team
2. Start with Phase 1 (search controllers)
3. Test each refactored controller
4. Document any changes/issues
5. Move to Phase 2, then Phase 3

---

**Document Status:** Planning Phase  
**Last Updated:** 28/04/2026  
**Target Completion:** By end of this sprint  
**Assigned To:** Development Team

