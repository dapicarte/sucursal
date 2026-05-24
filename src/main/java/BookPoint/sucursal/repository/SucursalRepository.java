package BookPoint.sucursal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import BookPoint.sucursal.model.Sucursal;

public interface SucursalRepository extends JpaRepository<Sucursal, Long>{
    
}
