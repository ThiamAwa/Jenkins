package sn.dev.gestionetablissement.dao;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import sn.dev.gestionetablissement.model.Inscrire;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class InscriptionDoaImpl implements InscriptionDao{
    @Override
    public void flush() {

    }

    @Override
    public <S extends Inscrire> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Inscrire> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Inscrire> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Inscrire getOne(Long aLong) {
        return null;
    }

    @Override
    public Inscrire getById(Long aLong) {
        return null;
    }

    @Override
    public Inscrire getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Inscrire> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Inscrire> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Inscrire> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Inscrire> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Inscrire> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Inscrire> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Inscrire, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Inscrire> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Inscrire> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Inscrire> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<Inscrire> findAll() {
        return List.of();
    }

    @Override
    public List<Inscrire> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Inscrire entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Inscrire> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Inscrire> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Inscrire> findAll(Pageable pageable) {
        return null;
    }
}
