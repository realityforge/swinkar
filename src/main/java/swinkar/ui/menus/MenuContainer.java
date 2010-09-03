package swinkar.ui.menus;

import java.awt.Component;

public interface MenuContainer
{
  int getMenuComponentCount();

  Component getMenuComponent( int i );

  Component add( Component menu, int index );

  void revalidate();

  void remove( int index );

  String getMenuId();
}
