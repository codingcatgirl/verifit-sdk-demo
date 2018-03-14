package com.veryfit.sdkdemo.config;

public enum Test {
	TEST1(100), TEST2(200), TEST3(400), TEST4(300);

	private int index;

	private Test(int index) {
		this.index = index;
	}

	public static Test valueOf(int index) {
		for (int i = 0; i < values().length; i++) {
			if (values()[i].index == index) {
				return values()[i];
			}
		}
		return values()[0];
	}
}
