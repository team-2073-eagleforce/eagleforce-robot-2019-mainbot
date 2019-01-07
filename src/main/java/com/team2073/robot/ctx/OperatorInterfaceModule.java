package com.team2073.robot.ctx;

import com.google.inject.AbstractModule;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.name.Names;
import edu.wpi.first.wpilibj.Joystick;

import static com.team2073.robot.ctx.RobotMapModule.*;

public class OperatorInterfaceModule extends AbstractModule {

	@Override
	protected void configure() {
		bindNamed(Joystick.class, "controller").toInstance(new Joystick(0));
		bindNamed(Joystick.class, "joystick").toInstance(new Joystick(1));
		bindNamed(Joystick.class, "wheel").toInstance(new Joystick(2));
	}

	private <T> LinkedBindingBuilder<T> bindNamed(Class<T> clazz, String name) {
		return bind(clazz).annotatedWith(Names.named(name));
	}

}
