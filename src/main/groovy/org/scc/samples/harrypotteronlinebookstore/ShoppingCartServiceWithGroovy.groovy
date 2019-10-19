package org.scc.samples.harrypotteronlinebookstore

class ShoppingCartServiceWithGroovy {

  def getDiscountedPrice = { List<Book> books ->

//  first bundle without double books
    def firstBundle = books.toUnique()

//  second bundle with double books
    def secondBundle = books.countBy { it }.findAll { it.value > 1 }.collect { it.key }

//  reorder bundles for better discount
    if (firstBundle.size() == 5 && secondBundle.size() == 3) {
      def bigBundle = reorderBundlesForBetterDiscount firstBundle, secondBundle
      firstBundle = bigBundle.get 0
      secondBundle = bigBundle.get 1
    }
    def priceForBook = 8.0
//  calculate price for first bundle
    def fullPriceFirstBundle = firstBundle.size() * priceForBook
    def discountPriceFirstBundle = calculateDiscount firstBundle, fullPriceFirstBundle

//  calculate price for second bundle
    def fullPriceSecondBundle = secondBundle.size() * priceForBook
    def discountPriceSecondBundle = calculateDiscount secondBundle, fullPriceSecondBundle

//  calculate full price
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
