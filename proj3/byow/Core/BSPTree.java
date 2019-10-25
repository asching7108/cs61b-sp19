package byow.Core;

import java.util.*;

/**
 * A BSP (Binary Space Partition) Tree that splits horizontally or vertically
 * (randomly decided) to the given number of leaves, randomly creates rooms,
 * and generates hallways that connect all rooms.
 */
public class BSPTree {
    private static final int ROOM_SPLIT_MIN = 6;
    private static final int ROOM_SIZE_MIN = 4;

    Random r;                   // random seed
    Leaf root;                  // the root leaf
    LinkedList<Room> rooms;     // list of rooms
    LinkedList<Room> hallways;  // list of hallways

    /**
     * Generates the BSPTree and initializes all member variables.
     *
     * @param w the width of the total space
     * @param h the height of the total space
     * @param leafNum the number of leaves
     * @param r the random seed
     */
    BSPTree(int w, int h, int leafNum, Random r) {
        this.r = r;
        root = new Leaf(0, 0, w, h);
        rooms = new LinkedList<>();
        hallways = new LinkedList<>();
        /* Splits the leaves with breadth first search order. */
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

    /**
     * Stores the space of the leaf, the split direction of its children leaves, the
     * children leaves and the randomly generated room.
     */
    class Leaf {
        int x;          // xOffset
        int y;          // yOffset
        int w;          // width
        int h;          // height
        int direction;  // 0: horizontal, 1: vertical, -1:initial
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

        /**
         * Creates a room with 80% possibility. The size and position of the room
         * is randomly decided on given requirements.
         */
        void createRoom() {
            if (RandomUtils.uniform(r) > 0.8) {
                return;
            }
            /* The width and height of the room are randomly decided but no less than
             * the ROOM_SIZE_MIN or half the leaf width / height + 2. */
            int min_width = Math.max(ROOM_SIZE_MIN, w / 2 + 2);
            int min_height = Math.max(ROOM_SIZE_MIN, h / 2 + 2);
            int width = RandomUtils.uniform(r, w - min_width + 1) + min_width;
            int height = RandomUtils.uniform(r, h - min_height + 1) + min_height;
            /* The xOffset and yOffset of the room are randomly decided and keep the
             * room within the range of the leaf. */
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

    /**
     * Splits the leaf into two children leaves. Randomly decides the split direction
     * and the width / height of the children leaves.
     *
     * @param leaf the leaf
     * @return {@code true} if the leaf is split, {@code false} otherwise
     */
    private boolean split(Leaf leaf) {
        // leaf is already split
        if (leaf.left != null || leaf.right != null) {
            return false;
        }
        // leaf is too small to split
        if (leaf.w < ROOM_SPLIT_MIN * 2 && leaf.h < ROOM_SPLIT_MIN * 2) {
            return false;
        }
        // decides split direction (horizontal or vertical)
        int direction = RandomUtils.uniform(r) < 0.5 ? 0 : 1;
        int length = direction == 0 ? leaf.w : leaf.h;
        if (length < ROOM_SPLIT_MIN * 2) {
            direction = (direction + 1) % 2;
            length = direction == 0 ? leaf.w : leaf.h;
        }
        leaf.direction = direction;
        // splits into two leaves with random size no less than the ROOM_SPLIT_MIN
        int split = RandomUtils.uniform(r, length - ROOM_SPLIT_MIN * 2 + 1) + ROOM_SPLIT_MIN;
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

    /**
     * Recursively creates rooms of the leaves without children.
     *
     * @param leaf the root leaf
     */
    private void createRooms(Leaf leaf) {
        if (leaf.left == null && leaf.right == null) {
            leaf.createRoom();
            if (leaf.room != null) {
                rooms.add(leaf.room);
            }
            return;
        }
        createRooms(leaf.left);
        createRooms(leaf.right);
    }

    /**
     * Recursively creates hallways that connect each two leaves of the same parent.
     * Each hallway starts from the center of a leaf and goes to the center of the
     * other leaf.
     *
     * @param leaf the root leaf
     */
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

    /**
     * Returns rooms.
     *
     * @return the rooms
     */
    public List<Room> rooms() { return rooms; }

    /**
     * Returns hallways.
     *
     * @return the hallways
     */
    public List<Room> hallways() { return hallways; }

}
