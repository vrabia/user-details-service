package app.vrabia.userdetilsservice.mappers;

import app.vrabia.userdetilsservice.dto.response.FriendshipDTO;
import app.vrabia.userdetilsservice.model.Friendship;
import app.vrabia.userdetilsservice.model.PendingFriendship;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = "spring")
public interface FriendshipMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user1", source = "sender")
    @Mapping(target = "user2", source = "receiver")
    Friendship pendingFriendshipToFriendship(PendingFriendship pendingFriendship);

    @Mapping(target = "friend", ignore = true)
    @Mapping(target = "status", expression = "java(app.vrabia.userdetilsservice.dto.response.FriendshipStatus.ACCEPTED)")
    FriendshipDTO friendshipToFriendshipDTO(Friendship friendship);

    @Mapping(target = "friend", source = "receiver")
    @Mapping(target = "friendsSince", ignore = true)
    @Mapping(target = "status", expression = "java(app.vrabia.userdetilsservice.dto.response.FriendshipStatus.SENT)")
    FriendshipDTO pendingSentFriendshipToFriendshipDTO(PendingFriendship pendingFriendship);

    @Mapping(target = "friend", source = "sender")
    @Mapping(target = "friendsSince", ignore = true)
    @Mapping(target = "status", expression = "java(app.vrabia.userdetilsservice.dto.response.FriendshipStatus.RECEIVED)")
    FriendshipDTO pendingReceivedFriendshipToFriendshipDTO(PendingFriendship pendingFriendship);
}
