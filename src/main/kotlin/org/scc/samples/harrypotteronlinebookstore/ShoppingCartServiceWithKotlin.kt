package org.scc.samples.harrypotteronlinebookstore


class ShoppingCartServiceWithKotlin {

    data class Book(val id: Int, val name: String, val price: Double)

    private fun organizeBooksIntoBundles(books: List<Book>): List<List<Book>> {
        return adjustBundles(books.fold(emptyList(), this::putBookIntoBundle))
    }

    private fun adjustBundles(bundles: List<List<Book>>): List<List<Book>> {
        val bundleWithThreeBooks = bundles.filter { it.size == 3 }.flatten()
        val bundleWithFiveBooks = bundles.filter { it.size == 5 }.flatten()
        return when {
            bundleWithThreeBooks.isNotEmpty() && bundleWithFiveBooks.isNotEmpty()
            -> reorderBundlesForBetterDiscount(bundleWithThreeBooks, bundleWithFiveBooks)
            else -> bundles
        }
    }

    private fun reorderBundlesForBetterDiscount(bundleWithThreeBooks: List<Book>, bundleWithFiveBooks: List<Book>): List<List<Book>> {
        val sameBooks = bundleWithThreeBooks
        val differentBooks = bundleWithFiveBooks - sameBooks
        val bundleOneWithFourBooks = bundleWithThreeBooks + differentBooks.last()
        val bundleTwoWithFourBooks = bundleWithFiveBooks - differentBooks.last()
        return listOf(bundleOneWithFourBooks, bundleTwoWithFourBooks)
    }

    private fun putBookIntoBundle(booksBundle: List<List<Book>>, book: Book): List<List<Book>> {
        val foundSet = booksBundle.find { booksAreDifferent(it.plus(book)) } ?: emptyList()
        val newAcc = when {
            foundSet.isEmpty() -> booksBundle.plusElement(listOf(book))
            else -> booksBundle.minusElement(foundSet).plusElement(foundSet.plusElement(book))
        }
        return newAcc.sortedByDescending { it.size }
    }


    fun getDiscountedPrice(books: List<Book>): Double =
            this.organizeBooksIntoBundles(books).map { discountedPriceForSet(it) }.sum()


    private fun discountedPriceForSet(books: List<Book>): Double {
        val discount = getDiscount(books)
        val price = getFullPrice(books)
        return calculateDiscount(discount, price)
    }

    private fun getFullPrice(books: List<Book>): Double = books.fold(0.00) { price, book -> price + book.price }

    private fun calculateDiscount(discount: Int, fullPrice: Double): Double = fullPrice - fullPrice * discount / 100

    private fun getDiscount(books: List<Book>): Int {
        val discounts = mapOf(Pair(2, 5), Pair(3, 10), Pair(4, 20), Pair(5, 25))
        return when {
            booksCouldBeDiscountable(books) -> discounts.getOrDefault(books.size, 0)
            else -> 0
        }
    }

    private fun booksCouldBeDiscountable(books: List<Book>) = booksAreDifferent(books)

    private fun booksAreDifferent(books: List<Book>) =
            books.distinct().size == books.size

}
