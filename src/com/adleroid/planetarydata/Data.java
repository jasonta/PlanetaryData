package com.adleroid.planetarydata;

public class Data {
	static class Details {
		Details(String title, int resId, String description) {
			this.title = title;
			this.resId = resId;
			this.description = description;
		}

		final String title;
		final int resId;
		final String description;

		@Override
		public String toString() {
			return "Details [title=" + title + ", resId=" + resId + ", description=" + description + "]";
		}
	}

	final static Details[] DETAILS = {
			new Details(
					"Mercury",
					R.drawable.mercury,
					"Mercury (0.4 AU from the Sun) is the closest planet to the Sun and the smallest planet in the Solar System (0.055 Earth masses). Mercury has no natural satellites, and its only known geological features besides impact craters are lobed ridges or rupes, probably produced by a period of contraction early in its history. Mercury's almost negligible atmosphere consists of atoms blasted off its surface by the solar wind. Its relatively large iron core and thin mantle have not yet been adequately explained. Hypotheses include that its outer layers were stripped off by a giant impact and that it was prevented from fully accreting by the young Sun's energy."),
			new Details(
					"Venus",
					R.drawable.venus,
					"Venus (0.7 AU from the Sun) is close in size to Earth (0.815 Earth masses) and, like Earth, has a thick silicate mantle around an iron core, a substantial atmosphere, and evidence of internal geological activity. However, it is much drier than Earth, and its atmosphere is ninety times as dense. Venus has no natural satellites. It is the hottest planet, with surface temperatures over 400\u00B0C (752\u00B0F), most likely due to the amount of greenhouse gases in the atmosphere. No definitive evidence of current geological activity has been detected on Venus, but it has no magnetic field that would prevent depletion of its substantial atmosphere, which suggests that its atmosphere is regularly replenished by volcanic eruptions."),
			new Details(
					"Earth",
					R.drawable.earth,
					"Earth (1 AU from the Sun) is the largest and densest of the inner planets, the only one known to have current geological activity, and the only place where life is known to exist. Its liquid hydrosphere is unique among the terrestrial planets, and it is also the only planet where plate tectonics has been observed. Earth's atmosphere is radically different from those of the other planets, having been altered by the presence of life to contain 21% free oxygen. It has one natural satellite, the Moon, the only large satellite of a terrestrial planet in the Solar System."),
			new Details(
					"Mars",
					R.drawable.mars,
					"Mars (1.5 AU from the Sun) is smaller than Earth and Venus (0.107 Earth masses). It possesses an atmosphere of mostly carbon dioxide with a surface pressure of 6.1 millibars (roughly 0.6% of that of the Earth). Its surface, peppered with vast volcanoes such as Olympus Mons and rift valleys such as Valles Marineris, shows geological activity that may have persisted until as recently as 2 million years ago. Its red colour comes from iron oxide (rust) in its soil. Mars has two tiny natural satellites (Deimos and Phobos) thought to be captured asteroids."),
			new Details(
					"Jupiter",
					R.drawable.jupiter,
					"Jupiter (5.2 AU), at 318 Earth masses, is 2.5 times the mass of all the other planets put together. It is composed largely of hydrogen and helium. Jupiter's strong internal heat creates a number of semi-permanent features in its atmosphere, such as cloud bands and the Great Red Spot. Jupiter has 67 known satellites. The four largest, Ganymede, Callisto, Io, and Europa, show similarities to the terrestrial planets, such as volcanism and internal heating. Ganymede, the largest satellite in the Solar System, is larger than Mercury."),
			new Details(
					"Saturn",
					R.drawable.saturn,
					"Saturn (9.5 AU), distinguished by its extensive ring system, has several similarities to Jupiter, such as its atmospheric composition and magnetosphere. Although Saturn has 60% of Jupiter's volume, it is less than a third as massive, at 95 Earth masses, making it the least dense planet in the Solar System. The rings of Saturn are made up of small ice and rock particles. Saturn has 62 confirmed satellites; two of which, Titan and Enceladus, show signs of geological activity, though they are largely made of ice. Titan, the second-largest moon in the Solar System, is larger than Mercury and the only satellite in the Solar System with a substantial atmosphere."),
			new Details(
					"Uranus",
					R.drawable.uranus,
					"Uranus (19.6 AU), at 14 Earth masses, is the lightest of the outer planets. Uniquely among the planets, it orbits the Sun on its side; its axial tilt is over ninety degrees to the ecliptic. It has a much colder core than the other gas giants and radiates very little heat into space. Uranus has 27 known satellites, the largest ones being Titania, Oberon, Umbriel, Ariel, and Miranda."),
			new Details(
					"Neptune",
					R.drawable.neptune,
					"Neptune (30 AU), though slightly smaller than Uranus, is more massive (equivalent to 17 Earths) and therefore more dense. It radiates more internal heat, but not as much as Jupiter or Saturn. Neptune has 13 known satellites. The largest, Triton, is geologically active, with geysers of liquid nitrogen. Triton is the only large satellite with a retrograde orbit. Neptune is accompanied in its orbit by a number of minor planets, termed Neptune trojans, that are in 1:1 resonance with it."),
	};

}
