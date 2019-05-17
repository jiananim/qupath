/*-
 * #%L
 * This file is part of QuPath.
 * %%
 * Copyright (C) 2014 - 2016 The Queen's University of Belfast, Northern Ireland
 * Contact: IP Management (ipmanagement@qub.ac.uk)
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

package qupath.lib.common;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Create a thread factory that supports adding a prefix to the name and setting daemon status.
 * <p>
 * This helps with debugging, e.g. using visualvm
 * 
 * @author Pete Bankhead
 *
 */
public class ThreadTools {
	
	/**
	 * Create a named thread factory with a specified priority.
	 * 
	 * @param prefix
	 * @param daemon
	 * @param priority
	 * @return
	 */
	public static ThreadFactory createThreadFactory(String prefix, boolean daemon, int priority) {
		return createThreadFactory(prefix, daemon, priority);
	}
	
	/**
	 * Create a named thread factory with {@code Thread.NORM_PRIORITY}.
	 * 
	 * @param prefix
	 * @param daemon
	 * @return
	 */
	public static ThreadFactory createThreadFactory(String prefix, boolean daemon) {
		return new SimpleThreadFactory(prefix, daemon, Thread.NORM_PRIORITY);
	}
	
	
	static class SimpleThreadFactory implements ThreadFactory {
		
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private String prefix;
		private boolean daemon;
		private int priority;
	
		SimpleThreadFactory(final String prefix, final boolean daemon, final int priority) {
			SecurityManager s = System.getSecurityManager();
			if (s == null)
				group = Thread.currentThread().getThreadGroup();
			else
				group = s.getThreadGroup();
			this.prefix = prefix;
			this.daemon = daemon;
			this.priority = Math.max(Thread.MIN_PRIORITY, Math.min(Thread.MAX_PRIORITY, priority));
					
		}
	
		@Override
		public Thread newThread(Runnable r) {
			String name = prefix + threadNumber.getAndIncrement();
			Thread t = new Thread(group, r, name, 0);
			t.setDaemon(daemon);
			if (t.getPriority() != priority)
				t.setPriority(priority);
			return t;
		}
		
	}
	
}