package com.tveritin.mapper;
import com.tveritin.bankservice.dto.CreateUserRequest;
import com.tveritin.bankservice.dto.CreateUserResponse;
import com.tveritin.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(CreateUserRequest request);

    @Mapping(source = "id", target = "userId")
    CreateUserResponse toResponse(User user);
}
