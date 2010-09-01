package swinkar;

import java.lang.reflect.Array;

@SuppressWarnings( { "unchecked" } )
public final class StaticLoophole
{
  public static <T> T cast( final Object object )
  {
    return (T)object;
  }

  public static <T> T[] newArray( final Class<?> type, final int size )
  {
    return (T[])Array.newInstance( type, size );
  }
}


