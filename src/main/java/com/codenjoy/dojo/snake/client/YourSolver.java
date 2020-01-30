package com.codenjoy.dojo.snake.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;

        System.out.println( board.toString());
        System.out.println("getBarriers: " + board.getBarriers());
        System.out.println("getWalls: " + board.getWalls());
        System.out.println("getSnakeDirection: " + board.getSnakeDirection());
        System.out.println("getSnake: " + board.getSnake());
        System.out.println("getHead: " + board.getHead());
        System.out.println("getStones: " + board.getStones());
        System.out.println("getApples: " + board.getApples());
        System.out.println("isGameOver: " + board.isGameOver());
        //----------------------------------
        List<Point> barriers = board.getBarriers();
        Direction snake_direction = board.getSnakeDirection();
        Point snake_head = board.getHead();
        List<Point> green_apples = board.getApples();
        Point green_apple = green_apples.stream().limit(1).findFirst().get();

        if(!board.isGameOver()){
            int snake_head_for_X = snake_head.getX();
            int snake_head_for_Y = snake_head.getY();

            int[][] barrier_points =
                    {
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                            {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
                    };

            barriers.forEach(barrier -> {
                barrier_points[barrier_points.length - barrier.getY() - 1][barrier.getX()] = 99;
            });
            return findPath( barrier_points, barrier_points.length - snake_head_for_Y - 1, snake_head_for_X,
                    barrier_points.length - green_apple.getY() - 1, green_apple.getX(),
                    board.getSnakeDirection());

        }

        return board.getSnakeDirection().toString();
    }

    public String findPath(int[][] map, int green_apple_X, int green_apple_Y, int snake_head_X, int snake_head_Y, Direction direction) {
        int[][] cloneMap = clone(map);
        List<Pair> wave = new ArrayList<Pair>();
        List<Pair> oldWave = new ArrayList<Pair>();

        oldWave.add(new Pair(green_apple_X, green_apple_Y));
        int nstep = 0;
        map[green_apple_X][green_apple_Y] = nstep;

        int[] dx = { 0, 1, 0, -1 };
        int[] dy = { -1, 0, 1, 0 };

        while (oldWave.size() > 0) {
            nstep++;
            wave.clear();
            for (Pair i : oldWave) {
                for (int d = 0; d < 4; d++) {
                    green_apple_X = i.x + dx[d];
                    green_apple_Y = i.y + dy[d];

                    if (map[green_apple_X][green_apple_Y] == -1) {
                        wave.add(new Pair(green_apple_X, green_apple_Y));
                        map[green_apple_X][green_apple_Y] = nstep;
                    }
                }
            }
            oldWave = new ArrayList<Pair>(wave);
        }

        for (int[] row : map) {
            System.out.println(Arrays.toString(row));
        }

        boolean flag = true;
        wave.clear();
        wave.add(new Pair(snake_head_X, snake_head_Y));

        while (map[snake_head_X][snake_head_Y] != 0) {
            flag = true;
            for (int d = 0; d < 4; d++) {
                green_apple_X = snake_head_X + dx[d];
                green_apple_Y = snake_head_Y + dy[d];

                if (map[snake_head_X][snake_head_Y] - 1 == map[green_apple_X][green_apple_Y]) {
                    snake_head_X = green_apple_X;
                    snake_head_Y = green_apple_Y;
                    wave.add(new Pair(snake_head_X, snake_head_Y));
                    flag = false;
                    break;
                }
            }
            if (flag) {
                System.out.println("путь не найден!");
                break;
            }
        }

        map = cloneMap;
        System.out.println("ok");

        if(!flag){
            for (Pair i : wave) {
                map[i.x][i.y] = 0;
            }
            for (int[] row : map) {
                System.out.println(Arrays.toString(row));
            }
            for (Pair i : wave) {
                System.out.println("point_X = " + i.x + ", point_Y = " + i.y);
            }

            Pair pair = wave.get(wave.size() - 2);

            int x1 = pair.x;
            int y1 = pair.y;

            if (snake_head_Y == y1 && snake_head_X <= x1)
                return Direction.DOWN.toString();
            if (snake_head_Y == y1 && snake_head_X > x1)
                return Direction.UP.toString();
            if (snake_head_X == x1 && snake_head_Y >= y1)
                return Direction.LEFT.toString();
            if (snake_head_X == x1 && snake_head_Y < y1)
                return Direction.RIGHT.toString();
        } else {
            //if the snake did not find a way
        }

        return direction.toString();
    }

    private int[][] clone(int[][] map) {
        int[][] cloneMap = new int[map.length][map.length];
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[i].length; j++)
                cloneMap[i][j] = map[i][j];
        return cloneMap;
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                //"http://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=1234567890123456789",
                //"http://206.81.16.237/codenjoy-contest/board/player/rf5c99aenf4bs1vpduhc?code=3248878382477585240",
                "http://206.81.16.237/codenjoy-contest/board/player/c8fpf2ap6whr6z8ngqsq?code=4464732422152399690",
                new YourSolver(new RandomDice()),
                new Board());
    }
}
