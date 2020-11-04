package com.nomad.data.agent;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

//@Configuration
//@EnableAsync
@Slf4j
public class SpringAsyncConfig {

	@Bean(name = "threadPoolTaskExecutor", destroyMethod = "destory")
	public Executor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(3);
		taskExecutor.setMaxPoolSize(30);
		taskExecutor.setQueueCapacity(10);
		taskExecutor.setThreadNamePrefix("Executor-");
		taskExecutor.initialize();
		return new HandlingExecutor(taskExecutor); // HandlingExecutor로 wrapping 합니다.
	}
	
	public class HandlingExecutor implements AsyncTaskExecutor {
		private AsyncTaskExecutor executor;
		
		public HandlingExecutor(AsyncTaskExecutor executor) {
			this.executor = executor;
		}
		
		@Override
		public void execute(Runnable task) {
			executor.execute(createWrappedRunnable(task));
		}
		
		@Override
		public void execute(Runnable task, long startTimeout) {
			executor.execute(createWrappedRunnable(task), startTimeout);
		}
		
		@Override
		public Future<?> submit(Runnable task) {
			return executor.submit(createWrappedRunnable(task));
		}
		
		@Override
		public <T> Future<T> submit(Callable<T> task) {
			return executor.submit(createCallable(task));
		}
		
		private <T> Callable<T> createCallable(final Callable<T> task) {
			return new Callable<T>() {
				@Override
				public T call() throws Exception {
					try {
						return task.call();
					}catch(Exception ex) {
						hanlde(ex);
						throw ex;
					}
				}
			};
		}
		
		private Runnable createWrappedRunnable(final Runnable task) {
			return new Runnable() {
				@Override
				public void run() {
					try {
						task.run();
					}catch(Exception ex) {
						hanlde(ex);
					}
				}
			};
		}
		
		private void hanlde(Exception ex) {
			log.error(">>>>> Failed to execute task. ", ex);
		}
		
		@SuppressWarnings("unused")
		private void destroy() {
			if(executor instanceof ThreadPoolTaskExecutor) {
				((ThreadPoolTaskExecutor) executor).shutdown();
			}
		}
	}
}
