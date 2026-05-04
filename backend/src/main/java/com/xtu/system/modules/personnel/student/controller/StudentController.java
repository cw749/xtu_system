package com.xtu.system.modules.personnel.student.controller;

import com.xtu.system.common.api.ApiResponse;
import com.xtu.system.common.api.PageResponse;
import com.xtu.system.modules.personnel.student.dto.StudentAccountCreateRequest;
import com.xtu.system.modules.personnel.student.dto.StudentCreateRequest;
import com.xtu.system.modules.personnel.student.dto.StudentQueryRequest;
import com.xtu.system.modules.personnel.student.dto.StudentUpdateRequest;
import com.xtu.system.modules.personnel.student.service.StudentService;
import com.xtu.system.modules.personnel.student.vo.StudentDetailVO;
import com.xtu.system.modules.personnel.student.vo.StudentPageItemVO;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('personnel:student:view')")
    public ApiResponse<PageResponse<StudentPageItemVO>> getStudentPage(StudentQueryRequest request) {
        return ApiResponse.success(studentService.getStudentPage(request));
    }

    @GetMapping("/export")
    @PreAuthorize("hasAuthority('personnel:student:view')")
    public ResponseEntity<byte[]> exportStudents(StudentQueryRequest request) {
        byte[] content = studentService.exportStudents(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
        headers.setContentDisposition(ContentDisposition.attachment().filename("students.csv", StandardCharsets.UTF_8).build());
        return ResponseEntity.ok().headers(headers).body(content);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('personnel:student:view')")
    public ApiResponse<StudentDetailVO> getStudentDetail(@PathVariable Long id) {
        return ApiResponse.success(studentService.getStudentDetail(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('personnel:student:create')")
    public ApiResponse<Long> createStudent(@Valid @RequestBody StudentCreateRequest request) {
        return ApiResponse.success("创建成功", studentService.createStudent(request));
    }

    @PostMapping("/import")
    @PreAuthorize("hasAuthority('personnel:student:create')")
    public ApiResponse<Integer> importStudents(@RequestBody List<@Valid StudentCreateRequest> requests) {
        return ApiResponse.success("导入成功", studentService.importStudents(requests));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('personnel:student:update')")
    public ApiResponse<Boolean> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentUpdateRequest request) {
        studentService.updateStudent(id, request);
        return ApiResponse.success("更新成功", Boolean.TRUE);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('personnel:student:delete')")
    public ApiResponse<Boolean> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ApiResponse.success("删除成功", Boolean.TRUE);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('personnel:student:delete')")
    public ApiResponse<Boolean> deleteStudents(@RequestParam List<Long> ids) {
        studentService.deleteStudents(ids);
        return ApiResponse.success("批量删除成功", Boolean.TRUE);
    }

    @PostMapping("/{id}/account")
    @PreAuthorize("hasAuthority('personnel:student:account')")
    public ApiResponse<Long> createStudentAccount(@PathVariable Long id, @Valid @RequestBody StudentAccountCreateRequest request) {
        return ApiResponse.success("账号创建成功", studentService.createStudentAccount(id, request));
    }

    @DeleteMapping("/{id}/account")
    @PreAuthorize("hasAuthority('personnel:student:account')")
    public ApiResponse<Boolean> removeStudentAccount(@PathVariable Long id) {
        studentService.removeStudentAccount(id);
        return ApiResponse.success("账号注销成功", Boolean.TRUE);
    }
}
