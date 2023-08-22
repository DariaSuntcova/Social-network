package ru.effectivemobile.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.effectivemobile.dto.friend.FriendsResponse;
import ru.effectivemobile.entity.FriendshipStatus;
import ru.effectivemobile.service.FriendService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/friends")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/{id}")
    public void addFriends(
            Principal principal,
            @PathVariable long id) {

        friendService.addFriends(principal.getName(), id);
    }

    @GetMapping
    public List<FriendsResponse> getAll(
            Principal principal,
            @RequestParam FriendshipStatus friendshipStatus,
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {

        return friendService.getAllWithPageable(principal.getName(),friendshipStatus, pageNumber, pageSize);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFriends(
            Principal principal,
            @PathVariable long id) {

        friendService.deleteFriends(principal.getName(), id);
    }

}
