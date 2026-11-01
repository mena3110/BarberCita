package com.barbercita.service;

import com.barbercita.domain.Servicio;
import com.barbercita.repository.ServicioRepository;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final FirebaseStorageService firebaseStorageService;

    @Transactional(readOnly = true)
    public List<Servicio> getServicios(boolean activo) {
        if (activo) {
            return servicioRepository.findByActivoTrue();
        }
        List<Servicio> servicios = servicioRepository.findAll();
        servicios.sort((s1, s2)
                -> Boolean.compare(s2.isActivo(), s1.isActivo()));
        return servicios;
    }

    @Transactional(readOnly = true)
    public Optional<Servicio> getServicio(Integer idServicio) {
        return servicioRepository.findById(idServicio);

    }

    // Si idServicio está vacío se inserta,
    // si tiene valor se actualiza el registro
    @Transactional
public void save(Servicio servicio, MultipartFile imagenFile) {

    if (servicio.getIdServicio() != null) {

        Servicio servicioActual = servicioRepository
                .findById(servicio.getIdServicio())
                .orElse(null);

        if (servicioActual != null
                && (imagenFile == null || imagenFile.isEmpty())) {

            servicio.setImagenUrl(
                    servicioActual.getImagenUrl());
        }
    }

    servicio = servicioRepository.save(servicio);

    if (imagenFile != null && !imagenFile.isEmpty()) {

        try {

            String rutaImagen =
                    firebaseStorageService.uploadImage(
                            imagenFile,
                            "servicio",
                            servicio.getIdServicio());

            servicio.setImagenUrl(rutaImagen);

            servicioRepository.save(servicio);

        } catch (IOException e) {

            log.warn("No fue posible almacenar la imagen del servicio ID: "
                    + servicio.getIdServicio(), e);
        }
    }
}

    /// Eliminación lógica del servicio
   
    @Transactional
    public void delete(Integer idServicio) {

        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                                "El servicio no existe: " + idServicio));

        servicio.setActivo(false);

        servicioRepository.save(servicio);
    }

    @Transactional
    public void activar(Integer idServicio) {

        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(()
                        -> new IllegalArgumentException(
                                "El servicio no existe: " + idServicio));

        servicio.setActivo(true);

        servicioRepository.save(servicio);
    }
}
