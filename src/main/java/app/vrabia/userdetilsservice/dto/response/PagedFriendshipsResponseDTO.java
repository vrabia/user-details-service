package app.vrabia.userdetilsservice.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PagedFriendshipsResponseDTO {
    Integer totalPages;
    Integer currentPage;
    private List<FriendshipDTO> friendshipDTO;
}
