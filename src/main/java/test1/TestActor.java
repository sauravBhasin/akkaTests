package test1;

import akka.actor.UntypedActor;

class TestActor extends UntypedActor {
	public TestActor() {
	}

	@Override
	public void onReceive(Object msg) {
		getContext().sender().tell("bar", getContext().self());
	}
}
