package org.scc.samples.harrypotteronlinebookstore


import groovy.transform.Immutable
import groovy.transform.Sortable

@Immutable
@Sortable
class Book {
  int id
  String name
}

class ShoppingCartServiceWithGroovy {

  def getDiscountedPrice = { List<Book> books ->

    List<Book> firstBundle = books.toUnique()
    List<Book> secondBundle = books.countBy { it }.findAll { it.value > 1 }.collect { it.key }

//  reoder bundles for better discount
    if (firstBundle.size() == 5 && secondBundle.size() == 3) {
      def bigBundle = reorderBundlesForBetterDiscount(firstBundle, secondBundle)
      firstBundle = bigBundle.get(0)
      secondBundle = bigBundle.get(1)
    }
//  calculate price
    def fullPriceFirstBundle = firstBundle.size() * 8.0
    def discountPriceFirstBundle = calculateDiscount(firstBundle, fullPriceFirstBundle)
    def fullPriceSecondBundle = 0
    def discountPriceSecondBundle = 0

//  calculate price for second bundle
    if (!secondBundle.isEmpty()) {
      fullPriceSecondBundle = secondBundle.size() * 8.0
      discountPriceSecondBundle = calculateDiscount(secondBundle, fullPriceSecondBundle)
    }
    discountPriceFirstBundle + discountPriceSecondBundle
  }


  def reorderBundlesForBetterDiscount = { List<Book> bundleWithFiveBooks, List<Book> bundleWithThreeBooks ->
    def differentBooks = bundleWithFiveBooks - bundleWithThreeBooks
    def bundleOneWithFourBooks = bundleWithThreeBooks + differentBooks.last()
    def bundleTwoWithFourBooks = bundleWithFiveBooks - differentBooks.last()
    [bundleOneWithFourBooks, bundleTwoWithFourBooks]
  }

  def calculateDiscount = { List<Book> books, double fullPrice ->
    switch (books.size()) {
      case 1:
        return fullPrice
      case 2:
        return fullPrice * 0.95
      case 3:
        return fullPrice * 0.90
      case 4:
        return fullPrice * 0.80
      case 5:
        return fullPrice * 0.75
      default:
        return fullPrice
    }
  }
}
