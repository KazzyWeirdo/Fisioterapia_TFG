import { useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faDumbbell } from '@fortawesome/free-solid-svg-icons'
import { createExerciseTemplate } from '../services/exerciseTemplateService'
import { useLanguage } from '../contexts/LanguageContext'
import ExerciseList, { type ExerciseDraft, type SetDraft } from '../components/ExerciseList'
import styles from './CreateExerciseTemplatePage.module.css'
import PageTitle from '../components/PageTitle'

function newSet(): SetDraft {
  return { weightKg: '', reps: '', restTimeSeconds: '', rpe: 7 }
}

export default function CreateExerciseTemplatePage() {
  const navigate = useNavigate()
  const { t } = useLanguage()

  const nextId = useRef(1)
  function newExercise(): ExerciseDraft {
    return { id: nextId.current++, name: '', sets: [newSet()] }
  }

  const [templateName, setTemplateName] = useState('')
  const [exercises, setExercises] = useState<ExerciseDraft[]>(() => [newExercise()])
  const [submitting, setSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState<string | null>(null)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!templateName) return
    setSubmitting(true)
    setSubmitError(null)
    try {
      await createExerciseTemplate({
        name: templateName,
        exercises: exercises.map(ex => ({
          name: ex.name,
          sets: ex.sets.map((s, i) => ({
            setNumber: i + 1,
            weightKg: parseFloat(String(s.weightKg)) || 0,
            reps: parseInt(String(s.reps)) || 0,
            restTimeSeconds: parseInt(String(s.restTimeSeconds)) || 0,
            rpe: s.rpe,
          })),
        })),
      })
      navigate(-1)
    } catch {
      setSubmitError(t('template_error'))
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <form className={styles.page} onSubmit={handleSubmit}>

      <div>
        <PageTitle title={t('template_register_title')} subtitle={t('template_register_subtitle')} />
      </div>

      <div className={styles.section}>
        <div className={styles.sectionHeader}>
          <FontAwesomeIcon icon={faDumbbell} /> {t('template_section_general')}
        </div>
        <div className={styles.field}>
          <label className={styles.label} htmlFor="templateName">{t('template_name_label')}</label>
          <input
            id="templateName"
            type="text"
            className={styles.input}
            placeholder={t('template_name_placeholder')}
            value={templateName}
            onChange={e => setTemplateName(e.target.value)}
            required
          />
        </div>
      </div>

      <div className={styles.exercisesHeader}>
        <span className={styles.exercisesTitle}><FontAwesomeIcon icon={faDumbbell} /> {t('template_section_exercises')}</span>
      </div>

      <ExerciseList exercises={exercises} onChange={setExercises} />

      {submitError && <p className={styles.error}>{submitError}</p>}

      <div className={styles.footer}>
        <button
          type="button"
          className={styles.discardBtn}
          onClick={() => navigate(-1)}
        >
          {t('template_discard')}
        </button>
        <button type="submit" className={styles.submitBtn} disabled={submitting}>
          {t('template_submit')}
        </button>
      </div>

    </form>
  )
}
