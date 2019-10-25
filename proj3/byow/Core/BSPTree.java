package byow.Core;

import java.util.*;

public class BSPTree {
    private static final int MIN_SIZE = 6;
    private static final int ROOM_SIZE_MIN = 4;

    Random r;
    Leaf root;
    LinkedList<Room> rooms;
    LinkedList<Room> hallways;

    BSPTree(int w, int h, int leafNum, Random r) {
        this.r = r;
        root = new Leaf(0, 0, w, h);
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
        createHallway(root);
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

        Leaf(int x, int y, int w, int h) {
            this.x = x;
            this.y=  y;
            this.w = w;
            this.h = h;
            direction = -1;
            room = null;
            left = null;
            right = null;
        }

        Room createRoom() {
            if (RandomUtils.uniform(r) > 0.8) {
                return null;
            }
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
            return this.room;
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
        leaf.direction = direction;
        System.out.println(direction);
        // splits into two leaves with random size no less than the MIN_SIZE
        int split = RandomUtils.uniform(r, length - MIN_SIZE * 2 + 1) + MIN_SIZE;
        if (direction == 0) {
            leaf.left = new Leaf(leaf.x, leaf.y, split, leaf.h);
            leaf.right = new Leaf(leaf.x + split - 1, leaf.y, leaf.w - split, leaf.h);
        }
        else {
            leaf.left = new Leaf(leaf.x, leaf.y, leaf.w, split);
            leaf.right = new Leaf(leaf.x, leaf.y + split - 1, leaf.w, leaf.h - split);
        }
        return true;
    }

    private void createRooms(Leaf leaf) {
        if (leaf.left == null && leaf.right == null) {
            if (leaf.createRoom() != null) {
                rooms.add(leaf.room);
            }
        }
    }

    private void createHallway(Leaf leaf) {
        if (leaf.left == null && leaf.right == null) {
            return;
        }
        if (leaf.direction == 0) {
            int start = leaf.left.x + leaf.left.w / 2 - 1;
            int end = leaf.right.x + leaf.right.w / 2 + 1;
            hallways().add(new Room(start, leaf.y + leaf.h / 2 - 1, end - start + 1, 3));
        }
        else {
            int start = leaf.left.y + leaf.left.h / 2 - 1;
            int end = leaf.right.y + leaf.right.h / 2 + 1;
            hallways().add(new Room(leaf.x + leaf.w / 2 - 1, start, 3, end - start + 1));
        }
        createHallway(leaf.left);
        createHallway(leaf.right);
    }

    public List<Room> rooms() { return rooms; }

    public List<Room> hallways() { return hallways; }

}
