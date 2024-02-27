package com.example.demo.repository;

import com.example.demo.model.Book;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookDaoImpl implements BookRepository {
    private final SessionFactory factory;

    @Autowired
    public BookDaoImpl(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public Book save(Book book) {
        Session session;
        Transaction transaction = null;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Cant create a book: " + book);
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = factory.openSession()) {
            return session.createQuery("from Book", Book.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Cant find all books");
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Session session = factory.openSession()) {
            return Optional.ofNullable(session.get(Book.class, id));
        } catch (Exception e) {
            throw new RuntimeException("Can't get a book by id " + id, e);
        }
    }
}
