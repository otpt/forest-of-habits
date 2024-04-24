package hh.forest_of_habits.mapper;

import hh.forest_of_habits.dto.response.UserInfoResponse;
import hh.forest_of_habits.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Mapping(source = "name", target = "username")
    public abstract UserInfoResponse map(User s);
}
