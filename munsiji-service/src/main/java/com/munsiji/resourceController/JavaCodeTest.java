package com.munsiji.resourceController;

import java.util.ArrayList;
import java.util.List;

public class JavaCodeTest {

	public static void main(String[] args) {
		System.out.println("Testing good:");
		List<Boolean> sendUpdate = new ArrayList(1);
		sendUpdate.add(true);
		sendUpdate.remove(0);
		sendUpdate.add(false);
		System.out.println(sendUpdate.get(0));

	}

}
