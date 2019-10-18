package org.scc.samples.harrypotteronlinebookstore

import groovy.transform.EqualsAndHashCode
import groovy.transform.Immutable
import groovy.transform.ToString

@Immutable
@EqualsAndHashCode
@ToString
class Book {
  int id
  String name
  double price
}

class ShoppingCartServiceWithGroovy {

  def getDiscountedPrice = { List<Book> books ->

    List<Book> firstBundle = books.unique(false)
    List<Book> secondBundle = books
    firstBundle.forEach { secondBundle.remove(books.indexOf(it)) }

    if (secondBundle.isEmpty()) {
      double fullPriceUniqueCart = calculateFullPrice firstBundle
      calculateDiscount firstBundle, fullPriceUniqueCart

    } else {
      if (firstBundle.size() == 5 && secondBundle.size() == 3) {
        def bigBundle = reorderBundlesForBetterDiscount(firstBundle, secondBundle)
        firstBundle = bigBundle.get(0)
        secondBundle = bigBundle.get(1)
      }
      def fullPriceUniqueCart = calculateFullPrice(firstBundle)
      def discountUniqueCart = calculateDiscount(firstBundle, fullPriceUniqueCart)

      def fullPriceSecondCart = calculateFullPrice(secondBundle)
      def discountSecondCart = calculateDiscount(secondBundle, fullPriceSecondCart)
      discountUniqueCart + discountSecondCart
    }
  }

  private calculateFullPrice = { List<Book> bundle ->
    bundle.collect { it.price }.flatten().sum() as double
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
        return discountPrice(fullPrice, 5)
      case 3:
        return discountPrice(fullPrice, 10)
      case 4:
        return discountPrice(fullPrice, 20)
      case 5:
        return discountPrice(fullPrice, 25)
      default:
        return fullPrice
    }
  }

  static def discountPrice = { double fullPrice, double discount ->
    return fullPrice - fullPrice * discount / 100
  }
}
