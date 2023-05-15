package com.isep.trippy.Models;

import java.io.Serializable;
import lombok.Data;

@Data
public class FriendId implements Serializable {
    private int userId;
    private int friendId;

    public FriendId() {}

    public FriendId(int userId, int friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
}

