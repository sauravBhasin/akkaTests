package test1;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

public class AskTest {

	public static void main(String[] args) throws Exception {
		ActorSystem sys = ActorSystem.apply("test");
		ActorRef ref = sys.actorOf(Props.create(TestActor.class), "mytest");
		Timeout t = new Timeout(5, TimeUnit.SECONDS);
		Future<Object> fut = Patterns.ask(ref, "foo", t);
		String response = (String) Await.result(fut, t.duration());
		System.out.println(response);
	}
}