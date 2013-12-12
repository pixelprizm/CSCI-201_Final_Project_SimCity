package city.restaurant.tanner;


public class Dish {

	public static float cookTime;
	public static String dishName;
	public static int menuNumber;
	public static int stock;
	public static int buffer;
	public static int max;
	public static float cost;
	
	public Dish(int mn)
	{
		menuNumber = mn;
		if(mn == 1)
		{
			stock = 1000;
			buffer = 3;
			max = 15;
			dishName = new String("Steak");
			cookTime = 30;
			cost = 15.99f;
			TannerRestaurant.menu.put(mn, this);
		}
		if(mn == 2)
		{
			stock = 1000;
			buffer = 3;
			max = 15;
			dishName = new String("Chicken");
			cookTime = 20;
			cost = 10.99f;
			TannerRestaurant.menu.put(mn,  this);
		}
		if(mn == 3)
		{
			stock = 1000;
			buffer = 3;
			max = 15;
			dishName = new String("Salad");
			cookTime = 5;
			cost = 5.99f;
			TannerRestaurant.menu.put(mn, this);
		}
		if(mn == 4)
		{
			stock = 1000;
			buffer = 3;
			max = 15;
			dishName = new String("Pizza");
			cookTime = 15;
			cost = 8.99f;
			TannerRestaurant.menu.put(mn, this);
		}
	}
}
