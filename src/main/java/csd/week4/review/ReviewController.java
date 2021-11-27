package csd.week4.review;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import csd.week4.book.BookNotFoundException;
import csd.week4.book.BookRepository;

@RestController
public class ReviewController {
    private ReviewRepository reviews;
    private BookRepository books;

    /**
     * This controller works with the repositories directly, without a service layer
     */
    public ReviewController(ReviewRepository reviews, BookRepository books){
        this.reviews = reviews;
        this.books = books;
    }

    @GetMapping("/books/{bookId}/reviews")
    public List<Review> getAllReviewsByBookId(@PathVariable (value = "bookId") Long bookId) {
        if(!books.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }
        return reviews.findByBookId(bookId);
    }

    /**
     * TODO: Activity 2
     * Implement this method to add a review for a given bookId
     * If there's no such book, throw a BookNotFoundException
     * 
     * Return the newly added review
     */
    @PostMapping("/books/{bookId}/reviews")
    public Review addReview(@PathVariable (value = "bookId") Long bookId, @RequestBody Review review) {
        // Hint: using "map" to handle the returned Optional object from "findById(bookId)"
        // your code here
        // need to change the "return null"
        // return books.findById(bookId).map(
        //     book -> {
        //         review.setBook(book);
        //         return reviews.save(review);
        //     }
        // ).orElseThrow(() -> new BookNotFoundException(bookId));
        Book book = books.findByBookId(bookId);
        if (book != null) {
            review.setBook(book);
            reviews.save(review);
        } else {
            throw new BookNotFoundException(bookId);
        }
    }

    /**
     * Update an existing review, given the bookId and the reviewId
     * Need to make sure that the review is associated with the book
     * 
     * TODO: Activity 2 - Spot a possible mistake in the below implementation
     * 
     * @param bookId
     * @param reviewId
     * @param newReview
     * @return
     */
    @PutMapping("/books/{bookId}/reviews/{reviewId}")
    public Review updateReview(@PathVariable (value = "bookId") Long bookId,
                                 @PathVariable (value = "reviewId") Long reviewId,
                                 @RequestBody Review newReview) {
        if(!books.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        } // reviews.findByIdAndBookId() instead of review.findById()
        return reviews.findByIdAndBookId(reviewId,bookId).map(review -> {
            review.setReview(newReview.getReview());
            return reviews.save(review);
        }).orElseThrow(() -> new ReviewNotFoundException(reviewId));
    }

    /**
     * Use a ResponseEntity to have more control over the response sent to client
     */
    @DeleteMapping("/books/{bookId}/reviews/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable (value = "bookId") Long bookId,
                              @PathVariable (value = "reviewId") Long reviewId) {
        
        if(!books.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        return reviews.findByIdAndBookId(reviewId, bookId).map(review -> {
            reviews.delete(review);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ReviewNotFoundException(reviewId));
    }
}