package test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThread {

	public static void main(String args[]) throws InterruptedException, ExecutionException {

		ExecutorService service = Executors.newFixedThreadPool(100);

		List<CompletableFuture<?>> completableFutures = new ArrayList<>();
		for (int i = 0; i < 100; ++i) {
			CompletableFuture<?> completableFuture = CompletableFuture.runAsync(() -> {
				System.out.println("running " + Thread.currentThread().getName());
				try {
					Thread.sleep(600000000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}, service);

			completableFutures.add(completableFuture);
		}

		for (CompletableFuture<?> future : completableFutures) {
			future.get();
		}
		service.shutdown();
	}
}
