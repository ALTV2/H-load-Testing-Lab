package com.tveritin.controller;

import com.tveritin.entity.Group;
import com.tveritin.service.GroupService;
import com.tveritin.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }


    @GetMapping
    public List<Group> getAllGroups() {
        return groupService.findAllGroups();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable UUID id) {
        return groupService.findGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        Group savedGroup = groupService.saveGroup(group);
        return ResponseEntity.ok(savedGroup);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable UUID id, @RequestBody Group groupDetails) {
        Group updatedGroup = groupService.updateGroup(id, groupDetails);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable UUID id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}