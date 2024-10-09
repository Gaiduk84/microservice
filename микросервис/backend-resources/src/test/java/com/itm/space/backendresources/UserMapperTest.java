package com.itm.space.backendresources;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testUserRepresentationToUserResponse() {
        // Создаем тестовые данные
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername("testUser");
        userRepresentation.setEmail("test@example.com");
        userRepresentation.setFirstName("Test");
        userRepresentation.setLastName("User");

        RoleRepresentation role1 = new RoleRepresentation();
        role1.setName("role1");
        RoleRepresentation role2 = new RoleRepresentation();
        role2.setName("role2");
        List<RoleRepresentation> roleList = List.of(role1, role2);

        GroupRepresentation group1 = new GroupRepresentation();
        group1.setName("group1");
        GroupRepresentation group2 = new GroupRepresentation();
        group2.setName("group2");
        List<GroupRepresentation> groupList = List.of(group1, group2);

        // Выполняем маппинг
        UserResponse userResponse = userMapper.userRepresentationToUserResponse(userRepresentation, roleList, groupList);

        // Проверяем результат
        assertEquals("test@example.com", userResponse.getEmail());
        assertEquals("Test", userResponse.getFirstName());
        assertEquals("User", userResponse.getLastName());
        assertEquals(List.of("role1", "role2"), userResponse.getRoles());
        assertEquals(List.of("group1", "group2"), userResponse.getGroups());
    }
}
