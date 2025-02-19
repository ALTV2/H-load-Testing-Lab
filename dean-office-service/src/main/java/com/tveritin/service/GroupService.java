package com.tveritin.service;

import com.tveritin.entity.Group;
import com.tveritin.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> findGroupById(UUID id) {
        return groupRepository.findById(id);
    }

    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    public void deleteGroup(UUID id) {
        groupRepository.deleteById(id);
    }

    public Group updateGroup(UUID id, Group groupDetails) {
        Group group = groupRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Group not found with id: " + id));

        group.setName(groupDetails.getName());
        group.setYears(groupDetails.getYears());
        group.setFaculty(groupDetails.getFaculty());

        return groupRepository.save(group);
    }
}