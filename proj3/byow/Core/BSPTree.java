package byow.Core;

import java.util.*;

public class BSPTree {
    private static final int MIN_SIZE = 4;
    private static final int ROOM_SIZE_MIN = 4;

    Random r;
    Leaf root;
    LinkedList<Room> rooms;
    LinkedList<Room> hallways;

    BSPTree(int w, int h, int leafNum, Random r) {
        this.r = r;
        root = new Leaf(0, 0, w, h, -1);
        rooms = new LinkedList<>();
        hallways = new LinkedList<>();
        ArrayDeque<Leaf> pq = new ArrayDeque<>();
        pq.add(root);
        leafNum--;
        while (leafNum > 0) {
            Leaf curr = pq.remove();
            if (split(curr)) {
                pq.add(curr.left);
                pq.add(curr.right);
                leafNum--;
            }
        }
        createRooms(root);
    }

    class Leaf {
        int x;
        int y;
        int w;
        int h;
        int direction;
        Room room;
        Leaf left;
        Leaf right;

        Leaf(int x, int y, int w, int h, int d) {
            this.x = x;
            this.y=  y;
            this.w = w;
            this.h = h;
            direction = d;
            room = null;
            left = null;
            right = null;
        }

        void createRoom() {
            int min_width = Math.max(ROOM_SIZE_MIN, w / 2 + 2);
            int min_height = Math.max(ROOM_SIZE_MIN, h / 2 + 2);
            int width = RandomUtils.uniform(r, w - min_width + 1) + min_width;
            int height = RandomUtils.uniform(r, h - min_height + 1) + min_height;
            int xOffset = x;
            int yOffset = y;
            if (w > width) {
                xOffset = x + RandomUtils.uniform(r, w - width);
            }
            if (h > height) {
                yOffset = y + RandomUtils.uniform(r, h - height);
            }
            this.room = new Room(xOffset, yOffset, width, height);
        }
    }

    private boolean split(Leaf leaf) {
        // leaf is already split
        if (leaf.left != null || leaf.right != null) {
            return false;
        }
        // leaf is too small to split
        if (leaf.w < MIN_SIZE * 2 && leaf.h < MIN_SIZE * 2) {
            return false;
        }
        // decides split direction (horizontal or vertical)
        int direction = RandomUtils.uniform(r) < 0.5 ? 0 : 1;
        int length = direction == 0 ? leaf.w : leaf.h;
        if (length < MIN_SIZE * 2) {
            direction = (direction + 1) % 2;
            length = direction == 0 ? leaf.w : leaf.h;
        }
        // splits into two leaves with random size no less than the MIN_SIZE
        int split = RandomUtils.uniform(r, length - MIN_SIZE * 2 + 1) + MIN_SIZE;
        if (direction == 0) {
            leaf.left = new Leaf(leaf.x, leaf.y, split, leaf.h, direction);
            leaf.right = new Leaf(leaf.x + split - 1, leaf.y, leaf.w - split, leaf.h, direction);
        }
        else {
            leaf.left = new Leaf(leaf.x, leaf.y, leaf.w, split, direction);
            leaf.right = new Leaf(leaf.x, leaf.y + split - 1, leaf.w, leaf.h - split, direction);
        }
        return true;
    }

    private Room createRooms(Leaf leaf) {
        if (leaf.left == null && leaf.right == null) {
            leaf.createRoom();
            rooms.add(leaf.room);
            return leaf.room;
        }
        Room leftRoom = createRooms(leaf.left);
        Room rightRoom = createRooms(leaf.right);
        createHallway(leftRoom, rightRoom);
        return RandomUtils.uniform(r) < 0.5 ? leftRoom : rightRoom;
    }

    private void createHallway(Room r1, Room r2) {
        boolean xOverlap = r1.xOffset() + r1.width() > r2.xOffset() + 2
                && r2.xOffset() + r2.width() > r1.xOffset() + 2;
        boolean yOverlap = r1.yOffset() + r1.height() > r2.yOffset() + 2
                && r2.yOffset() + r2.height() > r1.yOffset() + 2;
        if (xOverlap) {
            int overlapStart = Math.max(r1.xOffset(), r2.xOffset());
            int overlapEnd = Math.min(r1.xOffset() + r1.width() - 1, r2.xOffset() + r2.width() - 1);
            int x = RandomUtils.uniform(r, overlapEnd - overlapStart - 1) + overlapStart + 1;
            int y = Math.min(r1.yOffset() + r1.height(), r2.yOffset() + r2.height()) - 2;
            int length = Math.max(r1.yOffset(), r2.yOffset()) - y + 2;
            Room hallway = new Room(x - 1, y, 3, length);
            hallways.add(hallway);
        }
        if (yOverlap) {
            int overlapStart = Math.max(r1.yOffset(), r2.yOffset());
            int overlapEnd = Math.min(r1.yOffset() + r1.height() - 1, r2.yOffset() + r2.height() - 1);
            int y = RandomUtils.uniform(r, overlapEnd - overlapStart - 1) + overlapStart + 1;
            int x = Math.min(r1.xOffset() + r1.width(), r2.xOffset() + r2.width()) - 2;
            int length = Math.max(r1.xOffset(), r2.xOffset()) - x + 2;
            Room hallway = new Room(x, y - 1, length, 3);
            hallways.add(hallway);
        }
    }

    public List<Room> rooms() { return rooms; }

    public List<Room> hallways() { return hallways; }

}
