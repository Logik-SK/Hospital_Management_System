package com.sks.MediLabPro.mapper;

import com.sks.MediLabPro.dto.AppointmentTo;
import com.sks.MediLabPro.dto.BillingTo;
import com.sks.MediLabPro.dto.MedicalHistoryTo;
import com.sks.MediLabPro.dto.PatientTo;
import com.sks.MediLabPro.model.Appointment;
import com.sks.MediLabPro.model.Billing;
import com.sks.MediLabPro.model.MedicalHistory;
import com.sks.MediLabPro.model.Patient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Mapper {
    /**
     * @param appointmentTo
     * @return
     */
    public static Appointment toEntity(Optional<AppointmentTo> appointmentTo) {

        if (appointmentTo.isPresent()) {
            Appointment appointment = new Appointment(appointmentTo.get());
            if (appointmentTo.get().getPatientTo() != null) {
                appointment.setPatient(new Patient(appointmentTo.get().getPatientTo()));
                appointment.getPatient().setAppointments(Collections.singletonList(appointment));
            }
            return appointment;
        } else {
            return new Appointment();
        }

    }

    /**
     * @param patientTo
     * @return
     */
    public static Patient toEntity(PatientTo patientTo) {

        if (patientTo != null && patientTo.getAppointmentToList() != null) {
            Patient patient = new Patient(patientTo);
            List<Appointment> appointments = patientTo.getAppointmentToList().stream().map(appointmentTo -> {
                Appointment appointment = new Appointment(appointmentTo);
                appointment.setPatient(patient); // Establish relationship
                return appointment;
            }).collect(Collectors.toList());

            patient.setAppointments(appointments);

            return patient;
        } else {
            return new Patient();
        }

    }

    /**
     *
     * @param billingTo
     * @return
     */
    public static Billing toEntity(BillingTo billingTo) {
        if (billingTo == null) return new Billing();
        return new Billing(billingTo);
    }

    /**
     * Convert Patient to PatientTo
     *
     * @param patient
     * @return
     */
    public static PatientTo toDto(Patient patient) {
        PatientTo patientTo = null;
        if (patient == null) return new PatientTo();

        if (patient != null) {
            patientTo = new PatientTo(patient);
            List<AppointmentTo> appointmentToList = patient.getAppointments().stream().map(appointment -> {
                AppointmentTo appointmentTo = new AppointmentTo(appointment);
                return appointmentTo;
            }).collect(Collectors.toList());
            patientTo.setAppointmentToList(appointmentToList);

        }

        return patientTo;
    }


    /**
     * @param appointment
     * @return
     */
    public static AppointmentTo toDto(Optional<Appointment> appointment) {
        AppointmentTo appointmentTo = null;

        if (appointment.isEmpty()) return new AppointmentTo();

        appointmentTo = new AppointmentTo(appointment.get());
        if (appointment.get().getPatient() != null)
            appointmentTo.setPatientTo(new PatientTo(appointment.get().getPatient()));

        return appointmentTo;
    }

    /**
     * @param medicalHistory
     * @return
     */
    public static MedicalHistoryTo toDto(MedicalHistory medicalHistory) {
        MedicalHistoryTo medicalHistoryTo = null;

        if (medicalHistory != null) return new MedicalHistoryTo();

        // medicalHistoryTo = new MedicalHistoryTo(medicalHistory);


        return medicalHistoryTo;
    }

    public static BillingTo toDto(Billing billing) {
        if (billing == null) return new BillingTo();
        BillingTo billingTo = new BillingTo(billing);
        billingTo.addEntities("appointment", billing.getAppointment());
        return billingTo;
    }


}
