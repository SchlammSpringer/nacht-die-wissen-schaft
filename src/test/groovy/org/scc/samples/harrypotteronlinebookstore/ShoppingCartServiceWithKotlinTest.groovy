package org.scc.samples.harrypotteronlinebookstore

import spock.lang.Specification
import spock.lang.Unroll

import static org.scc.samples.harrypotteronlinebookstore.ShoppingCartServiceWithKotlin.Book

class ShoppingCartServiceWithKotlinTest extends Specification {


    @Unroll
    def "a specific cart of books costs #price after discount"() {

        given: "a cart of books"
        def cartOfBooks = [copiesOfBookOne(book1),
                           copiesOfBookTwo(book2),
                           copiesOfBookThree(book3),
                           copiesOfBookFour(book4),
                           copiesOfBookFive(book5)]
                .flatten() as List<Book>

        and: "the shopping cart service"
        def shoppingCartService = new ShoppingCartServiceWithKotlin()
        when: "the price for the cartWithNumberOfBooks is calculated"
        def discountedPrice = shoppingCartService.getDiscountedPrice cartOfBooks

        then: "the price should be #price"
        discountedPrice == price

        where: "given books matches the expected price"
        book1 | book2 | book3 | book4 | book5 | price
        1     | 0     | 0     | 0     | 0     | 8
        1     | 1     | 0     | 0     | 0     | 15.2
        1     | 1     | 1     | 0     | 0     | 21.6
        1     | 1     | 1     | 1     | 0     | 25.6
        1     | 1     | 1     | 1     | 1     | 30
        2     | 0     | 0     | 0     | 0     | 16
        2     | 1     | 0     | 0     | 0     | 23.2
        2     | 1     | 1     | 0     | 0     | 29.6
        2     | 2     | 2     | 1     | 1     | 51.2

    }

    def copiesOfBookOne(int copies) {
        copiesOfBook(copies, 1, "der Stein der Weisen")
    }

    def copiesOfBookTwo(int copies) {
        copiesOfBook(copies, 2, "die Kammer des Schreckens")
    }

    def copiesOfBookThree(int copies) {
        copiesOfBook(copies, 3, "der Gefangene von Askaban")
    }

    def copiesOfBookFour(int copies) {
        copiesOfBook(copies, 4, "der Feuerkelch")
    }

    def copiesOfBookFive(int copies) {
        copiesOfBook(copies, 5, "der Orden des Phönix")
    }

    def copiesOfBook = { int copies, int bookId, String bookName ->
        copies > 0 ? (1..copies).collect {
            new Book(bookId, bookName, 8)
        } : []
    }
}

