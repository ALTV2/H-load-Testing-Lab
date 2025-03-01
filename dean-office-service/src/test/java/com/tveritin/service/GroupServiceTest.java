package com.tveritin.service;

import com.tveritin.entity.Group;
import com.tveritin.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private GroupService groupService;

    private Group group;
    private UUID groupId;

    @BeforeEach
    void setUp() {
        groupId = UUID.randomUUID();
        group = new Group();
        group.setId(groupId);
        group.setName("Engineering");
        group.setYears(4);
        group.setFaculty("Science & Technology");
    }

    @Test
    void testFindAllGroups() {
        when(groupRepository.findAll()).thenReturn(List.of(group));

        List<Group> groups = groupService.findAllGroups();

        assertFalse(groups.isEmpty());
        assertEquals(1, groups.size());
        assertEquals(group, groups.get(0));

        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void testFindGroupById_Found() {
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));

        Optional<Group> foundGroup = groupService.findGroupById(groupId);

        assertTrue(foundGroup.isPresent());
        assertEquals(groupId, foundGroup.get().getId());

        verify(groupRepository, times(1)).findById(groupId);
    }

    @Test
    void testFindGroupById_NotFound() {
        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        Optional<Group> foundGroup = groupService.findGroupById(groupId);

        assertFalse(foundGroup.isPresent());

        verify(groupRepository, times(1)).findById(groupId);
    }

    @Test
    void testSaveGroup() {
        when(groupRepository.save(any(Group.class))).thenReturn(group);

        Group savedGroup = groupService.saveGroup(new Group());

        assertNotNull(savedGroup);
        assertEquals(group.getName(), savedGroup.getName());

        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void testDeleteGroup() {
        doNothing().when(groupRepository).deleteById(groupId);

        groupService.deleteGroup(groupId);

        verify(groupRepository, times(1)).deleteById(groupId);
    }

    @Test
    void testUpdateGroup_Found() {
        Group updatedGroup = new Group();
        updatedGroup.setName("Mathematics");
        updatedGroup.setYears(3);
        updatedGroup.setFaculty("Mathematical Sciences");

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(groupRepository.save(any(Group.class))).thenReturn(updatedGroup);

        Group result = groupService.updateGroup(groupId, updatedGroup);

        assertNotNull(result);
        assertEquals("Mathematics", result.getName());
        assertEquals(3, result.getYears());
        assertEquals("Mathematical Sciences", result.getFaculty());

        verify(groupRepository, times(1)).findById(groupId);
        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    void testUpdateGroup_NotFound() {
        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> groupService.updateGroup(groupId, group));
        assertEquals("Group not found with id: " + groupId, exception.getMessage());

        verify(groupRepository, times(1)).findById(groupId);
        verify(groupRepository, times(0)).save(any(Group.class));
    }
}
