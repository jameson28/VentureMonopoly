package venture.cs414.android.venturemonopoly.backEnd;

import java.util.ArrayList;
import java.util.List;

public class Player extends Owner{


	protected String name;
	private String token;
	private int location;
	private boolean isInJail;
	private int money;
	private Bank bank;
	protected int turnsInJail;
	protected int consecutiveTurns;

	public Player(String name) {
		this.name = name;
		bank = Bank.getInstance();
		this.money = 1500;
		this.location = 0;
		this.isInJail = false;
		turnsInJail = 0;
		consecutiveTurns = 0;
		propertiesOwned = new ArrayList<Property>();
	}
	
	public void movePlayer(int distance){
		int currentLocation = location; 
		int nextLocation = (location + distance) % 40;
		if(nextLocation < currentLocation){
			addMoney(200);
		}
		location = nextLocation;
	}
	
	public void payRent(Player owner, int amount){
		this.removeMoney(amount);
		owner.addMoney(amount);
	}

	public void buyHouse(Street street){
		if(bank.removeHouseFromBank()) {
			if(getMoney()>=street.getHouseCost()) {
				if(!street.placeHouse()){
					SystemMessagesToPlayer.getSystemMessage(6);
					return;
				}else{
					this.removeMoney(street.getHouseCost());
					return;
				}
			}else{
				SystemMessagesToPlayer.getSystemMessage(8);
				return;
			}
		}else{
			return;
		}

	}

	public void buyHotel(Street street) {
		if(bank.removeHotelFromBank()) {
			if(getMoney()>=street.getHotelCost()) {
				if(!street.placeHotel()){
					SystemMessagesToPlayer.getSystemMessage(7);
					return;
				}else{
					this.removeMoney(street.getHotelCost());
					return;
				}
			}else{
				SystemMessagesToPlayer.getSystemMessage(8);
				return;
			}
		}else{
			return;
		}
	}

	public void mortgage(Property property) {

		if(propertiesOwned.contains(property)){
			if(bank.mortgage(property)) {
				addMoney(property.getMortgageValue());
				return;
			}else{
				SystemMessagesToPlayer.getSystemMessage(9);
			}

		}

	}

	public void addProperty(Property property, int cost) {
		if(getMoney()>=cost) {
			propertiesOwned.add(property);
			this.money = this.money - cost;
		}else{
			SystemMessagesToPlayer.getSystemMessage(10);
		}
	}

	public void addProperty(Property property) {
		if (!propertiesOwned.contains(property)){
			propertiesOwned.add(property);
		}
	}

	public void removeProperty(Property property, int price) {
		if(propertiesOwned.contains(property)) {
			propertiesOwned.remove(property);
			this.money = this.money +price;
		}else{
			SystemMessagesToPlayer.getSystemMessage(11);
		}
	}

	public void removeProperty(Property property) {
		if(propertiesOwned.contains(property)) {
			propertiesOwned.remove(property);
		}else{
			SystemMessagesToPlayer.getSystemMessage(11);
		}
	}

	public void sellHouse(Street street) {
		if(street.removeHouse()){
			this.money = this.money + street.getHouseCost()/2;
			bank.addHouseToBank();
		}else{
			SystemMessagesToPlayer.getSystemMessage(12);
		}
	}

	public void sellHotels(Street street) {
		if(street.removeHotel()){
			this.money = this.money + street.getHotelCost()/2;
			bank.addHotelToBank();
		}else{
			SystemMessagesToPlayer.getSystemMessage(13);
		}
	}

	public void removeMoney(int amount){
		this.money = this.money-amount;
	}

	public void addMoney(int amount){
		this.money = this.money+amount;
	}

	public int getMoney(){
		return this.money;
	}

	public boolean getIsInJail(){
		return this.isInJail;
	}

	public void setIsInJail(boolean bool){
		this.isInJail=bool;
	}

	public String getName(){
		return this.name;
	}

	public String getToken(){
		return this.token;
	}

	public void setToken(String token){
		this.token = token;
	}
	
	public void setLocation(int location){
		this.location = location;
	}

	public int getLocation(){
		return this.location;
	}

	public void putInJail(){
		this.location=40;
		setIsInJail(true);
	}

	public void takeOutofJail(){
		this.location=10;
		setIsInJail(false);
		turnsInJail = 0;

	}

	//Ben Hamor:
	//I have written these methods specifically to use in JUnit tests
	@Override
	public boolean equals(Object other){
		if(other instanceof Player){
			Player otherPlayer = (Player) other;
			return this.name.equals(otherPlayer.getName());
		}

		return false;
	}
	
	public List<Property> getProperties(){
		return propertiesOwned;
	}

}
