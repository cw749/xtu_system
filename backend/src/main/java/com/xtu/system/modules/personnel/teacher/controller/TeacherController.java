package com.xtu.system.modules.personnel.teacher.controller;

import com.xtu.system.common.api.ApiResponse;
import com.xtu.system.common.api.PageResponse;
import com.xtu.system.modules.personnel.teacher.dto.TeacherAccountCreateRequest;
import com.xtu.system.modules.personnel.teacher.dto.TeacherCreateRequest;
import com.xtu.system.modules.personnel.teacher.dto.TeacherQueryRequest;
import com.xtu.system.modules.personnel.teacher.dto.TeacherUpdateRequest;
import com.xtu.system.modules.personnel.teacher.service.TeacherService;
import com.xtu.system.modules.personnel.teacher.vo.TeacherDetailVO;
import com.xtu.system.modules.personnel.teacher.vo.TeacherPageItemVO;
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
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('personnel:teacher:view')")
    public ApiResponse<PageResponse<TeacherPageItemVO>> getTeacherPage(TeacherQueryRequest request) {
        return ApiResponse.success(teacherService.getTeacherPage(request));
    }

    @GetMapping("/export")
    @PreAuthorize("hasAuthority('personnel:teacher:view')")
    public ResponseEntity<byte[]> exportTeachers(TeacherQueryRequest request) {
        byte[] content = teacherService.exportTeachers(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
        headers.setContentDisposition(ContentDisposition.attachment().filename("teachers.csv", StandardCharsets.UTF_8).build());
        return ResponseEntity.ok().headers(headers).body(content);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('personnel:teacher:view')")
    public ApiResponse<TeacherDetailVO> getTeacherDetail(@PathVariable Long id) {
        return ApiResponse.success(teacherService.getTeacherDetail(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('personnel:teacher:create')")
    public ApiResponse<Long> createTeacher(@Valid @RequestBody TeacherCreateRequest request) {
        return ApiResponse.success("创建成功", teacherService.createTeacher(request));
    }

    @PostMapping("/import")
    @PreAuthorize("hasAuthority('personnel:teacher:create')")
    public ApiResponse<Integer> importTeachers(@RequestBody List<@Valid TeacherCreateRequest> requests) {
        return ApiResponse.success("导入成功", teacherService.importTeachers(requests));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('personnel:teacher:update')")
    public ApiResponse<Boolean> updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherUpdateRequest request) {
        teacherService.updateTeacher(id, request);
        return ApiResponse.success("更新成功", Boolean.TRUE);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('personnel:teacher:delete')")
    public ApiResponse<Boolean> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ApiResponse.success("删除成功", Boolean.TRUE);
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('personnel:teacher:delete')")
    public ApiResponse<Boolean> deleteTeachers(@RequestParam List<Long> ids) {
        teacherService.deleteTeachers(ids);
        return ApiResponse.success("批量删除成功", Boolean.TRUE);
    }

    @PostMapping("/{id}/account")
    @PreAuthorize("hasAuthority('personnel:teacher:account')")
    public ApiResponse<Long> createTeacherAccount(@PathVariable Long id, @Valid @RequestBody TeacherAccountCreateRequest request) {
        return ApiResponse.success("账号创建成功", teacherService.createTeacherAccount(id, request));
    }

    @DeleteMapping("/{id}/account")
    @PreAuthorize("hasAuthority('personnel:teacher:account')")
    public ApiResponse<Boolean> removeTeacherAccount(@PathVariable Long id) {
        teacherService.removeTeacherAccount(id);
        return ApiResponse.success("账号注销成功", Boolean.TRUE);
    }
}
