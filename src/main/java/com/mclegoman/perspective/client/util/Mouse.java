/*
    Perspective
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Perspective
    Licence: GNU LGPLv3
*/

package com.mclegoman.perspective.client.util;

import com.google.common.collect.Lists;
import com.mclegoman.luminance.common.util.LogType;
import com.mclegoman.perspective.client.translation.Translation;
import com.mclegoman.perspective.common.data.Data;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.concurrent.Callable;

public class Mouse {
	private static boolean leftClicked;
	private static boolean middleClicked;
	private static boolean rightClicked;
	private static final Queue<Long> leftClickQueue = Lists.newLinkedList();
	private static final Queue<Long> middleClickQueue = Lists.newLinkedList();
	private static final Queue<Long> rightClickQueue = Lists.newLinkedList();
	public static void updateLeftClick(boolean clicked) {
		if (!clicked) leftClicked = false;
		else {
			if (!leftClicked) {
				leftClickQueue.add(System.currentTimeMillis() + 1000L);
				leftClicked = true;
			}
		}
	}
	public static void updateMiddleClick(boolean clicked) {
		if (!clicked) middleClicked = false;
		else {
			if (!middleClicked) {
				middleClickQueue.add(System.currentTimeMillis() + 1000L);
				middleClicked = true;
			}
		}
	}
	public static void updateRightClick(boolean clicked) {
		if (!clicked) rightClicked = false;
		else {
			if (!rightClicked) {
				rightClickQueue.add(System.currentTimeMillis() + 1000L);
				rightClicked = true;
			}
		}
	}
	public static int getLeftCPS() {
		return getClicksFromQueue(leftClickQueue);
	}
	public static int getMiddleCPS() {
		return getClicksFromQueue(middleClickQueue);
	}
	public static int getRightCPS() {
		return getClicksFromQueue(rightClickQueue);
	}
	public static int getClicksFromQueue(Queue<Long> queue) {
		while (!queue.isEmpty() && queue.peek() < System.currentTimeMillis()) queue.remove();
		return queue.size();
	}
	public static void clearQueues() {
		if (!leftClickQueue.isEmpty()) leftClickQueue.clear();
		if (!middleClickQueue.isEmpty()) middleClickQueue.clear();
		if (!rightClickQueue.isEmpty()) rightClickQueue.clear();
	}
	public static class ProcessCPS {
		private static final Map<Identifier, Callable<Boolean>> registry = new HashMap<>();
		public static void register(Identifier identifier, Callable<Boolean> callable) {
			registry.putIfAbsent(identifier, callable);
		}
		public static boolean shouldProcessCPS() {
			for (Map.Entry<Identifier, Callable<Boolean>> entry : registry.entrySet()) {
				try {
					if (entry.getValue().call()) return true;
				} catch (Exception error) {
					Data.version.sendToLog(LogType.ERROR, Translation.getString("Failed to process cps: {}", error));
				}
			}
			return false;
		}
	}
}