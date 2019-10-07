package co.com.soaint.correspondencia.business.control;

import co.com.soaint.correspondencia.domain.entity.CorRadicado;
import co.com.soaint.foundation.canonical.correspondencia.RadicadoDTO;
import co.com.soaint.foundation.framework.annotations.BusinessControl;
import co.com.soaint.foundation.framework.exceptions.SystemException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@BusinessControl
public class RadicadoControl extends GenericControl<CorRadicado> {

    private static Long serialVersionUID = 111111978L;

    public RadicadoControl() {
        super(CorRadicado.class);
    }

    public RadicadoDTO crearNumeroRadicado(String radicadoPadre) {
        log.info("Executing crearNumeroRadicado radicadoPadre = '{}'", radicadoPadre);
        if (!StringUtils.isEmpty(radicadoPadre)) {
            radicadoPadre = radicadoPadre.trim();
            BigInteger radicadoPadreBigInteger = new BigInteger(radicadoPadre);
            BigInteger maxConsecutivo = em.createNamedQuery("CorRadicado.findMaxConsecutivo", BigInteger.class)
                    .setParameter("RADICADO_PADRE", radicadoPadreBigInteger).getSingleResult();

            return RadicadoDTO.newInstance()
                    .numeroRadicado(radicadoPadre)
                    .consecutivo(maxConsecutivo.add(BigInteger.ONE)).build();

        }
        final TypedQuery<RadicadoDTO> prefijo = em.createNamedQuery("CorRadicado.findMaxRadicadoPadre", RadicadoDTO.class);
        final List<RadicadoDTO> resultList = prefijo.getResultList();
        log.info("resultList size = {}", resultList.size());
        if (resultList.isEmpty()) {
            return RadicadoDTO.newInstance()
                    .numeroRadicado("000001")
                    .consecutivo(BigInteger.ONE).build();
        }
        log.info("-----------------si esta pasando por aqui cangrejo---------");
        RadicadoDTO radicadoDTO = resultList.get(0);
        radicadoPadre = radicadoDTO.getNumeroRadicado();
        BigInteger radicadoPadreInteger = StringUtils.isEmpty(radicadoPadre) ? BigInteger.ONE : new BigInteger(radicadoPadre);
        radicadoPadreInteger = radicadoPadreInteger.add(BigInteger.ONE);

        log.info("-------------SERA ESTO"+ radicadoDTO.getConsecutivo());
        return RadicadoDTO.newInstance()
                .numeroRadicado(getLastDigit(radicadoPadreInteger, 6))
                .consecutivo(radicadoDTO.getConsecutivo()).build();
    }

    public RadicadoDTO getFechaRadicacion(String numeroRadicado) throws SystemException {
        try {
            if (StringUtils.isEmpty(numeroRadicado)) {
                throw new SystemException("No se ha especificado el numero de radicado");
            }
            final List<RadicadoDTO> resultList = em.createNamedQuery("CorRadicado.findFechRadByNumRad", RadicadoDTO.class)
                    .setParameter("NUM_RAD", "%" + numeroRadicado)
                    .getResultList();
            return !resultList.isEmpty() ? resultList.get(0) : RadicadoDTO.newInstance().fechaRadicacion(new Date()).build();
        } catch (Exception ex) {
            log.error("An error has occurred {}", ex.getMessage());
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }

    String getLastDigit(BigInteger value, int quantity) {
        final String valueString = value.toString();
        final String sufix = "000000" + valueString;
        return sufix.substring(sufix.length() - quantity);
    }

    public List<RadicadoDTO> listarRadicados(String nroRadicado, String nroIdentidad,String noGuia, String nombre, String anno, String tipoDocumento) {
        log.info("Listar Radicados: ");
        log.info("nroRadicado: " + nroRadicado);
        log.info("nroIdentidad: " + nroIdentidad);
        log.info("noGuia: " + noGuia);
        log.info("nombre: " + nombre);
        log.info("anno: " + anno);
        log.info("tipoDocumento: " + tipoDocumento);

        try {
            if (!StringUtils.isEmpty(nroRadicado) && !StringUtils.isEmpty(nroIdentidad) && !StringUtils.isEmpty(noGuia) && !StringUtils.isEmpty(nombre)) {
                log.info("-------------------------- CASO 1 -----------------------------------");
                final List<RadicadoDTO> resultList = em.createNamedQuery("CorRadicado.findRadicadosByRadPadreAndDocIdent", RadicadoDTO.class)
                        .setParameter("RADICADO_PADRE", new BigInteger(nroRadicado.split("-")[1]))
                        .setParameter("NRO_DOCU_IDENTIDAD", nroIdentidad + "%")
                        .setParameter("CONSECUTIVO", BigInteger.ONE)
                        .setParameter("NO_GUIA", noGuia)
                        .setParameter("NOMBRE", nombre + "%")
                        .getResultList();
                return listarRadicados(resultList);
            }
            if (StringUtils.isEmpty(nroRadicado) && !StringUtils.isEmpty(nroIdentidad)) {
                log.info("-------------------------- CASO 2 -----------------------------------");
                final List<RadicadoDTO> resultList = em.createNamedQuery("CorRadicado.findRadicadosByDocIdent", RadicadoDTO.class)
                        .setParameter("NRO_DOCU_IDENTIDAD", nroIdentidad + "%")
                        .setParameter("CONSECUTIVO", BigInteger.ONE)
                        .setParameter("TIP_DOC", tipoDocumento)
                        .getResultList();
                return listarRadicados(resultList);
            }
            if (!StringUtils.isEmpty(nroRadicado) && StringUtils.isEmpty(nroIdentidad)) {
                log.info("-------------------------- CASO 3 -----------------------------------");
                final List<RadicadoDTO> resultList = em.createNamedQuery("CorRadicado.findRadicadosByRadPadre", RadicadoDTO.class)
                        .setParameter("RADICADO_PADRE", new BigInteger(nroRadicado.split("-")[1]))
                        .setParameter("CONSECUTIVO", BigInteger.ONE)
                        .getResultList();
                return listarRadicados(resultList.stream().filter(radicadoDTO -> !StringUtils.isEmpty(radicadoDTO.getCodTipoPers())).collect(Collectors.toList()));
            }
            if (!StringUtils.isEmpty(noGuia) && StringUtils.isEmpty(nombre)) {
                log.info("-------------------------- CASO 4 -----------------------------------");
                final List<RadicadoDTO> resultList = em.createNamedQuery("CorRadicado.findNoGuia", RadicadoDTO.class)
                        .setParameter("CONSECUTIVO", BigInteger.ONE)
                        .setParameter("NO_GUIA", noGuia)
                        .getResultList();
                return listarRadicados(resultList);

            }
            if (StringUtils.isEmpty(noGuia) && !StringUtils.isEmpty(nombre)) {
                log.info("-------------------------- CASO 5 -----------------------------------");
                final List<RadicadoDTO> resultList = em.createNamedQuery("CorRadicado.findNombre", RadicadoDTO.class)
                        .setParameter("CONSECUTIVO", BigInteger.ONE)
                        .setParameter("NOMBRE", "%" + nombre + "%")
                        .getResultList();
                return listarRadicados(resultList);

            }
            if (!StringUtils.isEmpty(anno)) {

                final List<RadicadoDTO> resultList = em.createNamedQuery("CorRadicado.findAnnoRadicados", RadicadoDTO.class)
                        .setParameter("CONSECUTIVO", BigInteger.ONE)
                        .setParameter("ANNO_RADICADO", convertirFormtoDate(anno))
                        .getResultList();
                return listarRadicados(resultList);

            }
        } catch (Exception ex) {
            log.error("Error: ", ex);
        }
        return new ArrayList<>();
    }

    public Date convertirFormtoDate(String anno){

        SimpleDateFormat parseador = new SimpleDateFormat("yy-mm-dd");
        // el que formatea
        SimpleDateFormat formateador = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.ms");

        Date date = null;
        try {
            date = parseador.parse(anno+"-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public boolean existeRadicado(String nroRadicado) throws SystemException{
        try {
        if(org.springframework.util.StringUtils.isEmpty(nroRadicado)){
            return false;
        }
        List<CorRadicado> num_rad = em.createNamedQuery("CorRadicado.findRadicado", CorRadicado.class)
                .setParameter("NUM_RAD", "%" + nroRadicado + "%")
                .getResultList();

        if(org.springframework.util.ObjectUtils.isEmpty(num_rad)){
            return false;
        }
        return true;
        } catch (Exception ex) {
            log.error("An error has occurred {}", ex.getMessage());
            throw new SystemException("An error has occurred " + ex.getMessage());
        }
    }

    private List<RadicadoDTO> listarRadicados(final List<RadicadoDTO> resultList) {
        log.info("Listar Radicados: ", resultList);

        try {
            if (resultList.isEmpty()) {
                return resultList;
            }
            final LocalDate date = LocalDate.now();

            return resultList.stream().peek(radicadoDTO -> {
                String anno = getLastDigit(new BigInteger(String.valueOf(date.getYear())), 2);
                String radPadre = radicadoDTO.getNumeroRadicado();
                final String splitter = "-";
                if (radPadre.contains(splitter)) {
                    String[] split = radPadre.split(splitter);
                    radPadre = getLastDigit(new BigInteger(split[0]), 6) + splitter + split[1];
                }
                radicadoDTO.setNumeroRadicado(anno + splitter + radPadre);
            }).collect(Collectors.toList());
        } catch (Exception ex){
            log.error("Error: ", ex);
            return new ArrayList<>();
        }
    }



}
