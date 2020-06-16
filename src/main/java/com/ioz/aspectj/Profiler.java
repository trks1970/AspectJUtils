package com.ioz.aspectj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	public void reset()
	{
		profileData = new HashMap<>();
	}

	public String getProfileInfo()
	{
		StringBuilder builder = new StringBuilder();
		builder.append( "--- Profile Info ---" );
		for( String className : profileData.keySet() )
		{
			builder.append( "\r\n" );
			builder.append( className );
			builder.append( ":\r\n" );
			Map<String, TimeSpent> times = sortByValue( profileData.get( className ) );
			for( String method : times.keySet() )
			{
				builder.append( "   " );
				builder.append( method );
				builder.append( ":\r\n      " );
				TimeSpent ts = profileData.get( className ).get( method );
				builder.append( "invocations:" );
				builder.append( ts.getExecutionCount() );
				builder.append( "\r\n      " );
				builder.append( "total time:" );
				builder.append( ts.getTotalExecution() );
				builder.append( "\r\n      " );
				builder.append( "avg time:" );
				builder.append( ts.getTotalExecution() / ts.getExecutionCount() );
				builder.append( "\r\n      " );
				builder.append( "max time:" );
				builder.append( ts.getMaxExecution() );
				builder.append( "\r\n" );
			}
		}
		return builder.toString();
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

	private <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
	{
		List<Entry<K, V>> list = new ArrayList<>( map.entrySet() );
		list.sort( Entry.comparingByValue() );
		Map<K, V> result = new LinkedHashMap<>();
		for( Entry<K, V> entry : list )
		{
			result.put( entry.getKey(), entry.getValue() );
		}
		return result;
	}

	public class TimeSpent implements Comparable<TimeSpent>
	{
		private Long executionCount = 0L;
		private Long maxExecution = 0L;
		private Long totalExecution = 0L;

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

		@Override
		public int compareTo( TimeSpent o )
		{
			return totalExecution.compareTo( o.totalExecution );
		}

	}
}
