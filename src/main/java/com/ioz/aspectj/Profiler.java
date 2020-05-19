package com.ioz.aspectj;

import java.util.HashMap;
import java.util.Map;

public class Profiler
{
	private static final Profiler PROFILER = new Profiler();

	private Map<String, Map<String, TimeSpent>> profileData = new HashMap<>();

	private Profiler()
	{
	}

	public static Profiler getProfiler()
	{
		return PROFILER;
	}

	public Map<String, Map<String, TimeSpent>> getProfileData()
	{
		return profileData;
	}

	public void addExecution( String className, String methodName, long time )
	{
		TimeSpent ts = getEntry( className, methodName );
		ts.increase( time );
	}

	private TimeSpent getEntry( String className, String methodName )
	{
		Map<String, TimeSpent> map = profileData.get( className );
		if( map == null )
		{
			map = new HashMap<>();
			profileData.put( className, map );
		}
		TimeSpent ts = map.get( methodName );
		if( ts == null )
		{
			ts = new TimeSpent();
			map.put( methodName, ts );
		}
		return ts;
	}

	public class TimeSpent
	{
		private long executionCount;
		private long maxExecution;
		private long totalExecution;

		public long getExecutionCount()
		{
			return executionCount;
		}

		public long getMaxExecution()
		{
			return maxExecution;
		}

		public long getTotalExecution()
		{
			return totalExecution;
		}

		void increase( long time )
		{
			executionCount++;
			totalExecution += time;
			if( maxExecution < time )
			{
				maxExecution = time;
			}
		}

	}
}
