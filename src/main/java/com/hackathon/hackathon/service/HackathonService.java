package com.hackathon.hackathon.service;

import com.hackathon.hackathon.model.Bidder;
import com.hackathon.hackathon.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


/**
 * Para el desarrollo de la prueba:
 * <p>
 * (La lista de items ya viene inyectada en el servicio procedente del fichero MockDataConfig.java)
 * <p>
 * - Completa el cuerpo del método getItemsByType(String type) que recibiendo el parámetro type,
 * devuelva una lista de ítems del tipo especificado.
 * <p>
 * - Completa el cuerpo del método makeOffer(String itemName, double amount, Bidder bidder), que al recibir el
 * nombre del ítem (itemName), la cantidad de la oferta (amount), y el postor que realiza la oferta (bidder).
 * Comprueba si el ítem especificado por itemName existe en la lista de ítems:<br>
 * # Si el ítem no se encuentra, devuelve la constante ITEM_NOT_FOUND.<br>
 * # Si el ítem se encuentra, compara la oferta (amount) con la oferta más alta actual del ítem (highestOffer).<br>
 * # Si la oferta es mayor que la oferta más alta, actualiza la oferta más alta y el postor actual del ítem y
 * devuelve la constante OFFER_ACCEPTED.<br>
 * # Si la oferta es igual o menor que la oferta más alta, devuelve la constante OFFER_REJECTED.<br>
 * <p>
 * - Completa el cuerpo del método getWinningBidder() que debe devolver un Map de los Items en los que se
 * haya pujado (que existe un Bidder) y cuyo valor sea el nombre del Bidder que ha pujado.
 */

@Service
public class HackathonService {

    private static final String ITEM_NOT_FOUND = "Item not found";
    private static final String OFFER_ACCEPTED = "Offer accepted";
    private static final String OFFER_REJECTED = "Offer rejected";

    private final List<Item> items;

    @Autowired
    public HackathonService(List<Item> items) {
        this.items = items;
    }

    public List<Item> getAllItems() {
        return new ArrayList<>(items);
    }

    /**
     * Get a list of items filtering by the item type that is required.
     * If the type is not present, return the full list.
     *
     * @param type the item type
     * @return a filtered items list
     */
    public List<Item> getItemsByType(String type) {
        return items.stream().filter(item -> item.getType().equals(type.toLowerCase())).toList();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Makes an offer for an Item.
     *
     * @param itemName the name of the item for which the offer is made
     * @param amount   the amount the bidder wants to offer fot the iterm
     * @param bidder   the bidder who makes the offer
     * @return a message indicating the status of the offer (accepted or rejected) or if the item was not found.
     */
    public String makeOffer(String itemName, double amount, Bidder bidder) {
        // search for the item by its name
        Optional<Item> itemFound = items.stream()
                .filter(item -> item.getName().equals(itemName.toLowerCase()))
                .findFirst();
        // if not found, return "Item not found"
        if (itemFound.isEmpty()) return ITEM_NOT_FOUND;
        Item item = itemFound.get();
        // reject the offer if the current amount offer is equal or lees than the previous highest offer
        if (amount <= item.getHighestOffer()) return OFFER_REJECTED;
        item.setHighestOffer(amount); //set the actual highest offer
        item.setCurrentBidder(bidder); // set the bidder that makes the offer
        return OFFER_ACCEPTED;
    }

    /**
     * Get all items in witch the bidder wins the offer
     * @return a map with the name of the item as KEY and the name of the bidder as VALUE
     */
    public Map<String, String> getWinningBidder() {
        return items.stream()
                // get all items that have a bidder
                .filter(item -> Objects.nonNull(item.getCurrentBidder()))
                // collect the data in a Map by setting item's name as Key and bidder's name as Value
                .collect(Collectors.toMap(Item::getName, item -> item.getCurrentBidder().getName()));
    }
}
