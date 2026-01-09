package co.ke.bluestron.bsapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.bluestron.bsapi.entity.Venue;

public interface VenueRepository extends JpaRepository<Venue, Long> {
    // Basic CRUD operations provided by JpaRepository
}