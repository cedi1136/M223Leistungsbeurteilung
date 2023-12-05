package ch.zli.m223.service;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

import ch.zli.m223.model.Booking;

@ApplicationScoped
public class BookingService {

    @Inject
    EntityManager entityManager;

    @Transactional
    public Booking createBooking(@Valid Booking booking) {
        return entityManager.merge(booking);
    }

    @Transactional
    public void deleteBooking(Long id) {
        var entity = entityManager.find(Booking.class, id);
        if (checkIfUserIsAllowedToDeleteBooking(entity.getApplicationUser().getId(), id)
                || entity.getApplicationUser().getUserRole() == ch.zli.m223.model.ApplicationUser.UserRole.ADMIN) {
            entityManager.remove(entity);
        } else {
            throw new RuntimeException("User is not allowed to delete this booking");
        }
    }

    @Transactional
    public Booking updateBooking(Long id, Booking booking) {
        booking.setId(id);
        return entityManager.merge(booking);
    }

    public List<Booking> findAll() {
        var query = entityManager.createQuery("FROM Booking", Booking.class);
        return query.getResultList();
    }

    public Booking findBookingById(Long id) {
        return entityManager.find(Booking.class, id);
    }

    public boolean checkIfUserIsAllowedToDeleteBooking(Long userId, Long bookingId) {
        var query = entityManager.createQuery("FROM Booking WHERE id = :bookingId AND applicationUser_id = :userId",
                Booking.class);
        query.setParameter("bookingId", bookingId);
        query.setParameter("userId", userId);
        return query.getResultList().size() > 0;
    }
}
